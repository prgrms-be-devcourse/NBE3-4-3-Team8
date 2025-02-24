package com.ll.nbe342team8.domain.cart.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty

data class CartRequestDto(
    @field:NotEmpty(message = "장바구니 항목은 최소 1개 이상이어야 합니다")
    @field:Valid
    val cartItems: List<CartItemRequestDto>
)
