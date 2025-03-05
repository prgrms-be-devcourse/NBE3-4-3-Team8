package com.ll.nbe342team8.domain.order.order.controller

import com.ll.nbe342team8.domain.jwt.AuthService
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.domain.order.order.dto.*
import com.ll.nbe342team8.domain.order.order.entity.Order
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
    private val authService: AuthService
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

    // 주문 등록 (일반 주문: 장바구니 기반)
    @PostMapping("/create")
    fun createOrder(
        @RequestBody @Valid orderRequestDto: OrderRequestDto,
        @AuthenticationPrincipal securityUser: SecurityUser
    ): ResponseEntity<OrderResponseDto> {
        println("orderRequestDto = $orderRequestDto")
        val member: Member = securityUser.member
        val order: Order = orderService.createOrder(member, orderRequestDto)
        return ResponseEntity.ok(OrderResponseDto.from(order))
    }

    // 주문 등록 (단일 상품 주문)
    @PostMapping("/create/fast")
    fun createFastOrder(
        @RequestBody @Valid orderRequestDto: OrderRequestDto,
        @RequestParam("bookId") bookId: Long,
        @RequestParam("quantity") quantity: Int,
        @AuthenticationPrincipal securityUser: SecurityUser
    ): ResponseEntity<OrderResponseDto> {
        println("orderRequestDto = $orderRequestDto, bookId = $bookId, quantity = $quantity")
        val member: Member = securityUser.member
        val order: Order = orderService.createFastOrder(member, orderRequestDto, bookId, quantity)
        return ResponseEntity.ok(OrderResponseDto.from(order))
    }

    // 결제 정보 조회
    @GetMapping("/payment")
    fun payment(@AuthenticationPrincipal securityUser: SecurityUser): ResponseEntity<PaymentResponseDto> {
        val member: Member = securityUser.member
        val paymentResponseDto: PaymentResponseDto = orderService.createPaymentInfo(member)
        return ResponseEntity.ok(paymentResponseDto)
    }
}