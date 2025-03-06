package com.ll.nbe342team8.domain.order.order.controller;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.jwt.AuthService;
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.dto.OrderRequestDto;
import com.ll.nbe342team8.domain.order.order.dto.OrderResponseDto;
import com.ll.nbe342team8.domain.order.order.dto.PaymentResponseDto;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.domain.order.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/my/orders")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final AuthService authService;

    /**
     * 주문 목록 조회
     */
    @GetMapping
    public Page<OrderDTO> getOrdersByMember(@AuthenticationPrincipal SecurityUser securityUser, Pageable pageable) {
        Member member = securityUser.getMember();
        return orderService.getOrdersByMember(member, pageable);
    }

    /**
     * 주문 삭제
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long orderId,
            @CookieValue(value = "accessToken", required = false) String token) {
        Member member = authService.validateTokenAndGetMember(token);
        orderService.deleteOrder(orderId, member);
        return ResponseEntity.ok("주문 삭제 완료");
    }

    /**
     * 통합된 주문 생성 API
     * 장바구니 주문과 바로 주문 모두 처리
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @AuthenticationPrincipal SecurityUser securityUser) {
        Member member = securityUser.getMember();
        Order order = orderService.createOrder(member, orderRequestDto);
        return ResponseEntity.ok(OrderResponseDto.from(order));
    }

    /**
     * 통합된 결제 정보 조회 API
     */
    @GetMapping("/payment-info")
    public ResponseEntity<PaymentResponseDto> getPaymentInfo(
            @RequestParam OrderRequestDto.OrderType orderType,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Integer quantity,
            @AuthenticationPrincipal SecurityUser securityUser) {
        Member member = securityUser.getMember();
        PaymentResponseDto paymentInfo = orderService.createPaymentInfo(
                member, orderType, bookId, quantity);
        return ResponseEntity.ok(paymentInfo);
    }

    /**
     * 기존 장바구니 주문 API - 새 API로 리다이렉트
     * @deprecated 새로운 통합 API를 사용하세요
     */
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> legacyCreateOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @AuthenticationPrincipal SecurityUser securityUser) {

        // 장바구니 주문 타입으로 설정
        OrderRequestDto updatedRequest = new OrderRequestDto(
                orderRequestDto.postCode(),
                orderRequestDto.fullAddress(),
                orderRequestDto.recipient(),
                orderRequestDto.phone(),
                orderRequestDto.paymentMethod(),
                null,
                null,
                OrderRequestDto.OrderType.CART
        );

        return createOrder(updatedRequest, securityUser);
    }

    /**
     * 기존 바로 주문 API - 새 API로 리다이렉트
     * @deprecated 새로운 통합 API를 사용하세요
     */
    @PostMapping("/create/fast")
    public ResponseEntity<OrderResponseDto> legacyCreateFastOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @RequestParam("bookId") Long bookId,
            @RequestParam("quantity") int quantity,
            @AuthenticationPrincipal SecurityUser securityUser) {

        // 바로 주문 타입으로 설정
        OrderRequestDto updatedRequest = new OrderRequestDto(
                orderRequestDto.postCode(),
                orderRequestDto.fullAddress(),
                orderRequestDto.recipient(),
                orderRequestDto.phone(),
                orderRequestDto.paymentMethod(),
                bookId,
                quantity,
                OrderRequestDto.OrderType.DIRECT
        );

        return createOrder(updatedRequest, securityUser);
    }

    /**
     * 기존 장바구니 결제 정보 API - 새 API로 리다이렉트
     * @deprecated 새로운 통합 API를 사용하세요
     */
    @GetMapping("/payment")
    public ResponseEntity<PaymentResponseDto> legacyPayment(
            @AuthenticationPrincipal SecurityUser securityUser) {
        return getPaymentInfo(
                OrderRequestDto.OrderType.CART,
                null,
                null,
                securityUser
        );
    }

    /**
     * 기존 바로 결제 정보 API - 새 API로 리다이렉트
     * @deprecated 새로운 통합 API를 사용하세요
     */
    @GetMapping("/payment/single")
    public ResponseEntity<PaymentResponseDto> legacySinglePayment(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestParam("bookId") Long bookId,
            @RequestParam("quantity") int quantity) {
        return getPaymentInfo(
                OrderRequestDto.OrderType.DIRECT,
                bookId,
                quantity,
                securityUser
        );
    }
}