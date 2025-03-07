package com.ll.nbe342team8.domain.order.order.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.cart.dto.CartResponseDto;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.cart.service.CartService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DeliveryStatus;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository;
import com.ll.nbe342team8.domain.order.order.dto.OrderCacheDto;
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.dto.OrderRequestDto;
import com.ll.nbe342team8.domain.order.order.dto.PaymentResponseDto;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.domain.order.order.entity.Order.OrderStatus;
import com.ll.nbe342team8.domain.order.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final DetailOrderRepository detailOrderRepository;
    private final CartService cartService;
    private final BookService bookService;
    private final MemberService memberService;
    private final OrderCacheService orderCacheService;
    private final Random random = new Random();

    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByMember(Member member, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findByMember(member, pageable);
        if (ordersPage.isEmpty()) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }

        return ordersPage.map(order -> new OrderDTO(
                order.getId(),
                order.getOrderStatus().name(),
                order.getTotalPrice(),
                order.getCreateDate()));
    }

    @Transactional
    public void deleteOrder(Long orderId, Member member) {
        Order order = orderRepository.findByIdAndMember(orderId, member)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않거나 권한이 없습니다."));

        if (order.getOrderStatus() != OrderStatus.COMPLETE) {
            throw new IllegalStateException("주문이 완료되지 않아 삭제할 수 없습니다.");
        }

        detailOrderRepository.deleteByOrderId(orderId);
        orderRepository.delete(order);
    }

    /**
     * 통합된 주문 처리 메서드 - Redis 캐시 사용
     * 장바구니 결제와 바로 결제를 모두 처리하고 Redis에 임시 저장
     */
    @Transactional
    public String createOrder(Member member, OrderRequestDto orderRequestDto) {
        if (orderRequestDto.isCartOrder()) {
            return processCartOrderToCache(member, orderRequestDto);
        } else if (orderRequestDto.isDirectOrder()) {
            return processDirectOrderToCache(member, orderRequestDto);
        } else {
            throw new IllegalArgumentException("유효하지 않은 주문 유형입니다.");
        }
    }

    /**
     * 장바구니 주문 처리 - Redis 캐시에 저장
     */
    private String processCartOrderToCache(Member member, OrderRequestDto orderRequestDto) {
        List<Cart> cartList = cartService.findCartByMember(member);
        if (cartList.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어있습니다.");
        }

        Long totalPrice = calculateTotalPriceSales(cartList);

        // 상세 주문 정보 생성
        List<OrderCacheDto.DetailOrderCacheDto> detailOrderCacheDtos = cartList.stream()
                .map(cart -> OrderCacheDto.DetailOrderCacheDto.builder()
                        .bookId(cart.getBook().getId())
                        .quantity(cart.getQuantity())
                        .build())
                .collect(Collectors.toList());

        // 주문 캐시 DTO 생성
        OrderCacheDto orderCacheDto = OrderCacheDto.from(member, orderRequestDto, totalPrice, detailOrderCacheDtos);

        // Redis에 캐싱
        orderCacheService.cacheOrder(orderRequestDto.tossOrderId(), orderCacheDto);

        return orderRequestDto.tossOrderId();
    }

    /**
     * 바로 결제 주문 처리 - Redis 캐시에 저장
     */
    private String processDirectOrderToCache(Member member, OrderRequestDto orderRequestDto) {
        if (orderRequestDto.bookId() == null || orderRequestDto.quantity() == null) {
            throw new IllegalArgumentException("바로 결제 시 책 ID와 수량이 필요합니다.");
        }

        Book book = bookService.getBookById(orderRequestDto.bookId());
        long totalPrice = (long) book.getPricesSales() * orderRequestDto.quantity();

        // 상세 주문 정보 생성
        List<OrderCacheDto.DetailOrderCacheDto> detailOrderCacheDtos = List.of(
                OrderCacheDto.DetailOrderCacheDto.builder()
                        .bookId(book.getId())
                        .quantity(orderRequestDto.quantity())
                        .build()
        );

        // 주문 캐시 DTO 생성
        OrderCacheDto orderCacheDto = OrderCacheDto.from(member, orderRequestDto, totalPrice, detailOrderCacheDtos);

        // Redis에 캐싱
        orderCacheService.cacheOrder(orderRequestDto.tossOrderId(), orderCacheDto);

        return orderRequestDto.tossOrderId();
    }

    /**
     * 결제 완료 후 Redis 캐시에서 주문 정보를 가져와 DB에 저장
     */
    @Transactional
    public Order completeOrderFromCache(String tossOrderId) {
        // Redis에서 주문 정보 조회
        OrderCacheDto orderCacheDto = orderCacheService.getOrderFromCache(tossOrderId);
        if (orderCacheDto == null) {
            throw new IllegalArgumentException("주문 정보를 찾을 수 없습니다.");
        }

        // 회원 정보 조회
        Member member = memberService.getMemberById(orderCacheDto.getMemberId());

        // Order 엔티티 생성 및 저장
        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDERED)
                .fullAddress(orderCacheDto.getFullAddress())
                .postCode(orderCacheDto.getPostCode())
                .phone(orderCacheDto.getPhone())
                .recipient(orderCacheDto.getRecipient())
                .paymentMethod(orderCacheDto.getPaymentMethod())
                .totalPrice(orderCacheDto.getTotalPrice())
                .tossOrderId(tossOrderId)
                .build();

        orderRepository.save(order);

        // DetailOrder 엔티티 생성 및 저장
        List<DetailOrder> detailOrders = new ArrayList<>();
        for (OrderCacheDto.DetailOrderCacheDto detailDto : orderCacheDto.getDetailOrders()) {
            Book book = bookService.getBookById(detailDto.getBookId());
            DetailOrder detailOrder = DetailOrder.builder()
                    .order(order)
                    .deliveryStatus(DeliveryStatus.PENDING)
                    .book(book)
                    .bookQuantity(detailDto.getQuantity())
                    .build();
            detailOrders.add(detailOrder);
        }

        detailOrderRepository.saveAll(detailOrders);

        // 장바구니가 있는 경우 비우기 (장바구니 주문인 경우)
        if (orderCacheDto.getDetailOrders().size() > 1) {
            cartService.deleteProduct(member);
        }

        // Redis에서 캐시 삭제
        orderCacheService.deleteOrderFromCache(tossOrderId);

        return order;
    }

    private Order buildOrder(Member member, OrderRequestDto dto, Long totalPrice) {
        return Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDERED)
                .fullAddress(dto.fullAddress())
                .postCode(dto.postCode())
                .phone(dto.phone())
                .recipient(dto.recipient())
                .paymentMethod(dto.paymentMethod())
                .totalPrice(totalPrice)
                .tossOrderId(dto.tossOrderId())
                .build();
    }

    private DetailOrder buildDetailOrder(Order order, Book book, int quantity) {
        return DetailOrder.builder()
                .order(order)
                .deliveryStatus(DeliveryStatus.PENDING)
                .book(book)
                .bookQuantity(quantity)
                .build();
    }

    private Long calculateTotalPriceSales(List<Cart> cartList) {
        return cartList.stream()
                .mapToLong(cart -> (long) cart.getBook().getPricesSales() * cart.getQuantity())
                .sum();
    }

    private Long calculateTotalPriceStandard(List<Cart> cartList) {
        return cartList.stream()
                .mapToLong(cart -> (long) cart.getBook().getPriceStandard() * cart.getQuantity())
                .sum();
    }

    /**
     * 통합된 결제 정보 생성 메서드
     */
    public PaymentResponseDto createPaymentInfo(Member member, OrderRequestDto.OrderType orderType,
                                                Long bookId, Integer quantity) {
        if (OrderRequestDto.OrderType.CART.equals(orderType)) {
            return createCartPaymentInfo(member);
        } else if (OrderRequestDto.OrderType.DIRECT.equals(orderType)) {
            if (bookId == null || quantity == null) {
                throw new IllegalArgumentException("바로 결제 시 책 ID와 수량이 필요합니다.");
            }
            return createDirectPaymentInfo(member, bookId, quantity);
        } else {
            throw new IllegalArgumentException("유효하지 않은 주문 유형입니다.");
        }
    }

    /**
     * 장바구니 결제 정보 생성
     */
    private PaymentResponseDto createCartPaymentInfo(Member member) {
        List<Cart> cartList = cartService.findCartByMember(member);
        if (cartList.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어있습니다.");
        }

        List<CartResponseDto> cartResponseDtoList = cartList.stream()
                .map(CartResponseDto::from)
                .collect(Collectors.toList());

        return new PaymentResponseDto(
                cartResponseDtoList,
                calculateTotalPriceStandard(cartList),
                calculateTotalPriceSales(cartList),
                generateOrderId("CART"));
    }

    /**
     * 바로 결제 정보 생성
     */
    private PaymentResponseDto createDirectPaymentInfo(Member member, Long bookId, int quantity) {
        Book book = bookService.getBookById(bookId);
        Cart tempCart = new Cart(member, book, quantity);

        return new PaymentResponseDto(
                List.of(CartResponseDto.from(tempCart)),
                (long) book.getPriceStandard() * quantity,
                (long) book.getPricesSales() * quantity,
                generateOrderId("DIRECT"));
    }

    private String generateOrderId(String orderType) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNum = random.nextInt(1000);
        return String.format("%s_%s_%03d", orderType, timestamp, randomNum);
    }

    public Order getOrderById(String tossOrderId) {
        return orderRepository.findByTossOrderId(tossOrderId).orElse(null);
    }
}