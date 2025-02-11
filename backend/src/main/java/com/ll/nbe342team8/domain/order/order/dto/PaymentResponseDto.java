package com.ll.nbe342team8.domain.order.order.dto;

import com.ll.nbe342team8.domain.cart.dto.CartResponseDto;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PaymentResponseDto (
        List<CartResponseDto> cartList,
        Long priceStandard,
        Long pricesSales
){
}
