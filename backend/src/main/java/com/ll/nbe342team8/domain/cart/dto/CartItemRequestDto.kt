package com.ll.nbe342team8.domain.cart.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class CartItemRequestDto @JsonCreator constructor(
    @NotNull(message = "도서 ID는 필수입니다")
    @JsonProperty("bookId") val bookId: Long,

    @field:Min(1)
    @JsonProperty("quantity") val quantity: Int,

    @JsonProperty("isAddToCart") val isAddToCart: Boolean
)
