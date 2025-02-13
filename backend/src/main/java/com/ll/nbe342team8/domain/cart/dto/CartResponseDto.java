package com.ll.nbe342team8.domain.cart.dto;

import com.ll.nbe342team8.domain.cart.entity.Cart;

public record CartResponseDto(
        Long bookId,
        int quantity,
        String title,
        int price,
        String coverImage
) {

    public static CartResponseDto from(Cart cart) {
        return new CartResponseDto(
                cart.getBook().getId(),
                cart.getQuantity(),
                cart.getBook().getTitle(),
                cart.getBook().getPricesSales(),
                cart.getBook().getCoverImage()
        );
    }
}
