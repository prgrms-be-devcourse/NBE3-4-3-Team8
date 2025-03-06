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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/my/orders")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final AuthService authService;

    // 주문조회
    @GetMapping
    public Page<OrderDTO> getOrdersByMember(@AuthenticationPrincipal SecurityUser securityUser, Pageable pageable) {
        Member member = securityUser.getMember(); // 인증된 사용자의 Member 객체를 가져옴
        return orderService.getOrdersByMember(member, pageable);
    }

    // 주문삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId,
                                              @CookieValue(value = "accessToken", required = false) String token) {
        Member member = authService.validateTokenAndGetMember(token);
        orderService.deleteOrder(orderId, member);
        return ResponseEntity.ok("주문 삭제 완료");
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        log.info("[OrderController] createOrder 호출됨");
        log.info("[OrderController] orderRequestDto = {}", orderRequestDto);

        if (securityUser == null) {
            log.warn("[OrderController] securityUser가 null입니다. 인증 정보가 없습니다.");
            // 여기서 401을 직접 리턴해볼 수도 있음
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        Member member = securityUser.getMember();
        log.info("[OrderController] 인증된 사용자: {} (ID: {})", member.getEmail(), member.getId());

        Order order = orderService.createOrder(member, orderRequestDto);
        return ResponseEntity.ok(OrderResponseDto.from(order));
    }

    // 주문등록 (단일 상품 주문)
    @PostMapping("/create/fast")
    public ResponseEntity<OrderResponseDto> createFastOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @RequestParam("bookId") Long bookId,
            @RequestParam("quantity") int quantity,
            @AuthenticationPrincipal SecurityUser securityUser) {

        System.out.println("orderRequestDto = " + orderRequestDto + ", bookId = " + bookId + ", quantity = " + quantity);
        Member member = securityUser.getMember();
        Order order = orderService.createFastOrder(member, orderRequestDto, bookId, quantity);
        return ResponseEntity.ok(OrderResponseDto.from(order));
    }

    @GetMapping("/payment")
    public ResponseEntity<PaymentResponseDto> payment(@AuthenticationPrincipal SecurityUser securityUser) {
        Member member = securityUser.getMember();
        PaymentResponseDto paymentResponseDto = orderService.createPaymentInfo(member);
        System.out.println("paymentResponseDto = " + paymentResponseDto);
        return ResponseEntity.ok(paymentResponseDto);
    }

    @GetMapping("/payment/single")
    public ResponseEntity<PaymentResponseDto> payment(@AuthenticationPrincipal SecurityUser securityUser,
                                                      @RequestParam("bookId") Long bookId,
                                                      @RequestParam("quantity") int quantity) {
        Member member = securityUser.getMember();
        PaymentResponseDto paymentResponseDto = orderService.createSinglePaymentInfo(member, bookId, quantity);
        System.out.println("paymentResponseDto = " + paymentResponseDto);
        return ResponseEntity.ok(paymentResponseDto);
    }
}
