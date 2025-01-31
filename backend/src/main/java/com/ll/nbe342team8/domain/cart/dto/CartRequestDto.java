package com.ll.nbe342team8.domain.cart.dto;

import java.util.List;

public record CartRequestDto(
        List<CartItemRequestDto> cartItems
) {}
