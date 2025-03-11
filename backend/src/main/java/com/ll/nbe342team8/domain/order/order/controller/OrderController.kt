package com.ll.nbe342team8.domain.order.order.controller

import com.ll.nbe342team8.domain.jwt.AuthService
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO
import com.ll.nbe342team8.domain.order.order.dto.OrderRequestDto
import com.ll.nbe342team8.domain.order.order.dto.OrderRequestDto.OrderType
import com.ll.nbe342team8.domain.order.order.dto.PaymentResponseDto
import com.ll.nbe342team8.domain.order.order.service.OrderPayService
import com.ll.nbe342team8.domain.order.order.service.OrderService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/my/orders")
@CrossOrigin(origins = ["http://localhost:3000"])
class OrderController(
    private val orderService: OrderService,
    private val authService: AuthService,
    private val orderPayService: OrderPayService
) {

    // 주문 조회
    @GetMapping
    fun getOrdersByMember(
        @AuthenticationPrincipal securityUser: SecurityUser,
        pageable: Pageable
    ): Page<OrderDTO> {
        val member: Member = securityUser.member
        return orderService.getOrdersByMember(member, pageable)
    }

    @PostMapping
    fun createOrder(
        @RequestBody orderRequestDto: @Valid OrderRequestDto,
        @AuthenticationPrincipal securityUser: SecurityUser
    ): ResponseEntity<*> {
        val member: Member = securityUser.member
        val tossOrderId = orderPayService.createOrder(member, orderRequestDto)

        val response: MutableMap<String, String> = HashMap<String, String>()
        response.put("orderId", tossOrderId)

        return ResponseEntity.ok<MutableMap<String, String>>(response)
    }

    /**
     * 통합된 결제 정보 조회 API
     */
    @GetMapping("/payment-info")
    fun getPaymentInfo(
        @RequestParam orderType: OrderType,
        @RequestParam(required = false) bookId: Long?,
        @RequestParam(required = false) quantity: Int?,
        @AuthenticationPrincipal securityUser: SecurityUser
    ): ResponseEntity<PaymentResponseDto?> {
        val member: Member = securityUser.member
        val paymentInfo = orderPayService.createPaymentInfo(
            member, orderType, bookId, quantity
        )
        return ResponseEntity.ok<PaymentResponseDto>(paymentInfo)
    }
}