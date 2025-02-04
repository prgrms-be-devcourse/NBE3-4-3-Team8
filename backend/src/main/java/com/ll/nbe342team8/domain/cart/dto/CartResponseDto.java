package com.ll.nbe342team8.domain.cart.dto;

import com.ll.nbe342team8.domain.cart.entity.Cart;

public record CartResponseDto(
        Long memberId,
        Long bookId,
        int quantity,
        String title,
        int price,
        String coverImage
) {

    public static CartResponseDto from(Cart cart) {
        return new CartResponseDto(
                cart.getMember().getId(),
                cart.getBook().getId(),
                cart.getQuantity(),
                cart.getBook().getTitle(),
                cart.getBook().getPricesSales(),
                cart.getBook().getCoverImage()
        );
    }
}
