package com.ll.nbe342team8.domain.order.order.controller;

import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<List<OrderDTO>> getOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long memberId = securityUser.getMember().getId();

        List<OrderDTO> orders = orderService.getOrdersByMemberId(memberId);
        return ResponseEntity.ok(orders);
    }

    //주문삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long memberId = securityUser.getMember().getId();

        orderService.deleteOrder(orderId, memberId);
        return ResponseEntity.ok("주문 삭제 완료");
    }
}

