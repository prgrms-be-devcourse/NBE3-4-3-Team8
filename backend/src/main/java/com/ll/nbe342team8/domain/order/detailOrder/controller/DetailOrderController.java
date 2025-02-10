package com.ll.nbe342team8.domain.order.detailOrder.controller;

import com.ll.nbe342team8.domain.jwt.AuthService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.order.detailOrder.dto.DetailOrderDto;
import com.ll.nbe342team8.domain.order.detailOrder.service.DetailOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/my/orders")
public class DetailOrderController {
    private final DetailOrderService detailOrderService;
    private final AuthService authService;

    public DetailOrderController(DetailOrderService detailOrderService, AuthService authService) {
        this.detailOrderService = detailOrderService;
        this.authService = authService;
    }

    // 주문상세조회
    @GetMapping("/{orderId}/details")
    public ResponseEntity<List<DetailOrderDto>> getDetailOrders(@PathVariable Long orderId, @CookieValue(value = "accessToken", required = false) String token) {
        Member member = authService.validateTokenAndGetMember(token);
        Long memberId = member.getId();

        List<DetailOrderDto> detailOrders = detailOrderService.getDetailOrdersByOrderIdAndMemberId(orderId, memberId);
        return ResponseEntity.ok(detailOrders);
    }
}