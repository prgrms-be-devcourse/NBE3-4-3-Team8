package com.ll.nbe342team8.domain.cart.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CartRequestDto(

        @NotEmpty(message = "장바구니 항목은 최소 1개 이상이어야 합니다")
        @Valid
        List<CartItemRequestDto> cartItems
) {}
