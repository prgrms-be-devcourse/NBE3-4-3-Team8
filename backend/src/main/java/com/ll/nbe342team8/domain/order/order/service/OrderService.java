package com.ll.nbe342team8.domain.order.order.service;

import com.ll.nbe342team8.domain.cart.dto.CartResponseDto;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.cart.service.CartService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DeliveryStatus;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository;
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.dto.OrderRequestDto;
import com.ll.nbe342team8.domain.order.order.dto.PaymentResponseDto;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.domain.order.order.entity.Order.OrderStatus;
import com.ll.nbe342team8.domain.order.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final DetailOrderRepository detailOrderRepository;
    private final MemberRepository memberRepository;
    private final CartService cartService;


    @Autowired
    public OrderService(OrderRepository orderRepository, DetailOrderRepository detailOrderRepository, MemberRepository memberRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.detailOrderRepository = detailOrderRepository;
        this.memberRepository = memberRepository;
        this.cartService = cartService;
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByMember(Member member) {
        // 주문 조회
        List<Order> orders = orderRepository.findByMember(member);
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }

        // DTO로 변환하여 반환
        return orders.stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getOrderStatus().name(),
                        order.getTotalPrice()))
                .collect(Collectors.toList());
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

    @Transactional
    public Order createOrder(Member member, OrderRequestDto orderRequestDTO) {
        List<Cart> cartList = cartService.findCartByMember(member);

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDERED)
                .fullAddress(orderRequestDTO.fullAddress())
                .postCode(orderRequestDTO.postCode())
                .phone(orderRequestDTO.phone())
                .recipient(orderRequestDTO.recipient())
                .paymentMethod(orderRequestDTO.paymentMethod())
                .totalPrice(calculateTotalPriceSales(cartList))
                .build();

        orderRepository.save(order);

        List<DetailOrder> detailOrders = cartList.stream()
                .map(cart -> DetailOrder.builder()
                        .order(order)
                        .deliveryStatus(DeliveryStatus.PENDING)
                        .book(cart.getBook())
                        .bookQuantity(cart.getQuantity())
                        .build())
                .collect(Collectors.toList());

        detailOrderRepository.saveAll(detailOrders);

        cartService.deleteProduct(member); // 주문 완료 후 장바구니 비우기

        return order;
    }

    @Transactional
    public Order createFastOrder(Member member, OrderRequestDto orderRequestDTO) {
        List<Cart> cartList = cartService.findCartByMember(member);

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDERED)
                .fullAddress(orderRequestDTO.fullAddress())
                .postCode(orderRequestDTO.postCode())
                .phone(orderRequestDTO.phone())
                .recipient(orderRequestDTO.recipient())
                .paymentMethod(orderRequestDTO.paymentMethod())
                .totalPrice(calculateTotalPriceSales(cartList))
                .build();

        orderRepository.save(order);

        List<DetailOrder> detailOrders = cartList.stream()
                .map(cart -> DetailOrder.builder()
                        .order(order)
                        .deliveryStatus(DeliveryStatus.PENDING)
                        .book(cart.getBook())
                        .bookQuantity(cart.getQuantity())
                        .build())
                .collect(Collectors.toList());

        detailOrderRepository.saveAll(detailOrders);

        return order;
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

    public PaymentResponseDto createPaymentInfo(Member member) {
        List<Cart> cartList = cartService.findCartByMember(member);
        List<CartResponseDto> cartResponseDtoList = cartList.stream()
                .map(CartResponseDto::from)
                .collect(Collectors.toList());

        Long totalPriceSales = calculateTotalPriceSales(cartList);
        Long totalPriceStandard = calculateTotalPriceStandard(cartList);

        return new PaymentResponseDto(cartResponseDtoList, totalPriceStandard, totalPriceSales);
    }
}