package com.ll.nbe342team8.domain.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemRequestDto(

        @NotNull(message = "도서 ID는 필수입니다")
        Long bookId,

        @Min(1) int quantity,

        boolean isAddToCart
) {}
