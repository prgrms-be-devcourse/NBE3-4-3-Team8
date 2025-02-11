package com.ll.nbe342team8.domain.order.detailOrder.controller;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.order.detailOrder.dto.DetailOrderDto;
import com.ll.nbe342team8.domain.order.detailOrder.service.DetailOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my/orders")
public class DetailOrderController {
    private final DetailOrderService detailOrderService;

    // 주문 상세 조회 - Member와 orderId를 기반으로 조회
    @GetMapping("/{orderId}/details")
    public List<DetailOrderDto> getDetailOrdersByOrderIdAndMember(
            @PathVariable Long orderId,
            @AuthenticationPrincipal SecurityUser securityUser) {

        Member member = securityUser.getMember();
        return detailOrderService.getDetailOrdersByOrderIdAndMember(orderId, member);
    }
}