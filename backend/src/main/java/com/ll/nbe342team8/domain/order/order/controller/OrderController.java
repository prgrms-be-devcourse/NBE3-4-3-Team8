package com.ll.nbe342team8.domain.order.order.controller;

import com.ll.nbe342team8.domain.jwt.AuthService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.dto.OrderRequestDto;
import com.ll.nbe342team8.domain.order.order.dto.PaymentResponseDto;
import com.ll.nbe342team8.domain.order.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId,
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
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                         @AuthenticationPrincipal SecurityUser securityUser) {

        Member member = securityUser.getMember();
        String tossOrderId = orderService.createOrder(member, orderRequestDto);

        Map<String, String> response = new HashMap<>();
        response.put("orderId", tossOrderId);

        return ResponseEntity.ok(response);
    }

    /**
     * 통합된 결제 정보 조회 API
     */
    @GetMapping("/payment-info")
    public ResponseEntity<PaymentResponseDto> getPaymentInfo(@RequestParam OrderRequestDto.OrderType orderType,
                                                             @RequestParam(required = false) Long bookId,
                                                             @RequestParam(required = false) Integer quantity,
                                                             @AuthenticationPrincipal SecurityUser securityUser) {

        Member member = securityUser.getMember();
        PaymentResponseDto paymentInfo = orderService.createPaymentInfo(
                member, orderType, bookId, quantity);
        return ResponseEntity.ok(paymentInfo);
    }
}