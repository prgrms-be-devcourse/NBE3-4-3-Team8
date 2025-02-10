package com.ll.nbe342team8.domain.order.order.controller;

import com.ll.nbe342team8.domain.jwt.AuthService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/my/orders")
public class OrderController {
    private final OrderService orderService;
    private final AuthService authService;

    public OrderController(OrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    // 주문조회
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders(@CookieValue(value = "accessToken", required = false) String token) {
        Member member = authService.validateTokenAndGetMember(token);
        String oauthId = member.getOauthId(); // 여기서 oauthId를 가져옵니다.

        List<OrderDTO> orders = orderService.getOrdersByOauthId(oauthId);
        return ResponseEntity.ok(orders);
    }

    // 주문삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId, @CookieValue(value = "accessToken", required = false) String token) {
        Member member = authService.validateTokenAndGetMember(token);
        String oauthId = member.getOauthId(); // 여기서 oauthId를 가져옵니다.

        orderService.deleteOrder(orderId, oauthId);
        return ResponseEntity.ok("주문 삭제 완료");
    }
}