package com.ll.nbe342team8.domain.cart.controller

import com.ll.nbe342team8.domain.cart.dto.CartRequestDto
import com.ll.nbe342team8.domain.cart.dto.CartResponseDto
import com.ll.nbe342team8.domain.cart.service.CartService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart", description = "Cart API")
class CartController(
    private val cartService: CartService,
) {

    @Operation(summary = "장바구니 추가")
    @PostMapping
    fun addCart(
        @RequestBody cartRequestDto: CartRequestDto,
        @AuthenticationPrincipal securityUser: SecurityUser
    ) {
        val member = securityUser.member
        cartService.updateCartItems(member, cartRequestDto)
    }

    @Operation(summary = "장바구니 수정 json")
    @PutMapping
    fun updateCartItems(
        @AuthenticationPrincipal securityUser: SecurityUser,
        @RequestBody @Valid cartRequestDto: CartRequestDto
    ) {
        val member = securityUser.member
        cartService.updateCartItems(member, cartRequestDto)
    }

    @Operation(summary = "장바구니 삭제")
    @DeleteMapping
    fun deleteBook(
        @AuthenticationPrincipal securityUser: SecurityUser,
        @RequestBody cartRequestDto: CartRequestDto
    ) {
        val member = securityUser.member
        cartService.deleteProduct(member, cartRequestDto)
    }

    @Operation(summary = "장바구니 조회")
    @GetMapping
    fun getCart(@AuthenticationPrincipal securityUser: SecurityUser): List<CartResponseDto> {
        val member = securityUser.member
        val carts = cartService.findCartByMember(member)
        return carts.map { CartResponseDto.from(it) }
    }

    @PostMapping("/anonymous")
    fun getAnonymousCart(
        @RequestBody @Valid cartRequestDto: CartRequestDto
    ): ResponseEntity<List<CartResponseDto>> {
        val cartItems = cartService.getCartItems(cartRequestDto)
        val cartResponseDto = cartItems.map { CartResponseDto.from(it) }
        return ResponseEntity.ok(cartResponseDto)
    }
}
