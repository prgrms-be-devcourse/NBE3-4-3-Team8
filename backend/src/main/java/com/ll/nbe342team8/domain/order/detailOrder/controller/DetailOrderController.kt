package com.ll.nbe342team8.domain.order.detailOrder.controller

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.domain.order.detailOrder.dto.DetailOrderDto
import com.ll.nbe342team8.domain.order.detailOrder.service.DetailOrderService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/my/orders")
class DetailOrderController(
    private val detailOrderService: DetailOrderService
) {

    // 주문 상세 조회
    @GetMapping("/{orderId}/details")
    fun getDetailOrdersByOrderIdAndMember(
        @PathVariable orderId: Long,
        @AuthenticationPrincipal securityUser: SecurityUser
    ): List<DetailOrderDto> {

        val member: Member = securityUser.member
        val detailOrderDtoList = detailOrderService.getDetailOrdersByOrderIdAndMember(orderId, member)
        println("실행됨")
        println("orderId = $orderId")
        println("detailOrderDtoList = $detailOrderDtoList")
        println("출력 결과 end")

        return detailOrderDtoList
    }
}