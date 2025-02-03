package com.ll.nbe342team8.domain.order.order.controller;

import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.service.OrderService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam Long memberId) {// 추후 변경
        List<OrderDTO> orders = orderService.getOrdersByMemberId(memberId);
        return ResponseEntity.ok(orders);
    }

    //주문삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("주문 삭제 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("주문이 존재하지 않습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body("주문이 완료되지 않아 삭제할 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }
}