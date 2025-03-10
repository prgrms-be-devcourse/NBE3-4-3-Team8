package com.ll.nbe342team8.domain.cart.dto

import com.ll.nbe342team8.domain.cart.entity.Cart

data class CartResponseDto(
    val bookId: Long,
    val quantity: Int,
    val title: String,
    val price: Int,
    val coverImage: String
) {
    companion object {
        @JvmStatic
        fun from(cart: Cart): CartResponseDto {
            val book = cart.book
            val id = book.id ?: throw IllegalStateException("Book id is null")
            return CartResponseDto(
                bookId = id,
                quantity = cart.quantity,
                title = book.title,
                price = book.pricesSales,
                coverImage = book.coverImage
            )
        }
    }
}
