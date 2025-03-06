package com.ll.nbe342team8.domain.cart.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty

data class CartRequestDto @JsonCreator constructor(
    @field:NotEmpty(message = "장바구니 항목은 최소 1개 이상이어야 합니다")
    @field:Valid
    @JsonProperty("cartItems") val cartItems: List<CartItemRequestDto>
)
