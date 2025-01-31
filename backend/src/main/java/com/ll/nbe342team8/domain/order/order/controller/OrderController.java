package com.ll.nbe342team8.domain.order.order.controller;

import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/my/orders")
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam Long memberId) {
        List<OrderDTO> orders = orderService.getOrdersByMemberId(memberId);
        return ResponseEntity.ok(orders);
    }
}