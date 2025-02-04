package com.ll.nbe342team8.domain.cart.dto;

public record CartItemRequestDto(
        Long bookId,
        int quantity
) {}
