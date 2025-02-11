package com.ll.nbe342team8.domain.order.order.controller;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.dto.OrderRequestDto;
import com.ll.nbe342team8.domain.order.order.dto.OrderResponseDto;
import com.ll.nbe342team8.domain.order.order.dto.PaymentResponseDto;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.domain.order.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/my/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //주문조회
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam Long memberId) { // 추후 변경
        List<OrderDTO> orders = orderService.getOrdersByMemberId(memberId);
        return ResponseEntity.ok(orders);
    }

    //주문삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("주문 삭제 완료");
    }

    //주문등록
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                                        @AuthenticationPrincipal SecurityUser securityUser) {

        System.out.println("orderRequestDto = " + orderRequestDto);

        Member member = securityUser.getMember();
        Order order = orderService.createOrder(member, orderRequestDto);

        return ResponseEntity.ok(OrderResponseDto.from(order));
    }

    @GetMapping("/payment")
    public ResponseEntity<PaymentResponseDto> payment(@AuthenticationPrincipal SecurityUser securityUser) {

        Member member = securityUser.getMember();

        PaymentResponseDto paymentResponseDto = orderService.createPaymentInfo(member);

        return ResponseEntity.ok(paymentResponseDto);
    }
}

