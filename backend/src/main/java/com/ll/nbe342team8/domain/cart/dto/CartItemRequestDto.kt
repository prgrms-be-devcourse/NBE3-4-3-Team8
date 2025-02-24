package com.ll.nbe342team8.domain.cart.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class CartItemRequestDto(
    @NotNull(message = "도서 ID는 필수입니다")
    val bookId: Long,

    @field:Min(1)
    val quantity: Int,

    val isAddToCart: Boolean
)
