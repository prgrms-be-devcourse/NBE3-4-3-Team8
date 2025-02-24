package com.ll.nbe342team8.domain.cart.service

import com.ll.nbe342team8.domain.book.book.service.BookService
import com.ll.nbe342team8.domain.cart.dto.CartRequestDto
import com.ll.nbe342team8.domain.cart.dto.CartResponseDto
import com.ll.nbe342team8.domain.cart.entity.Cart
import com.ll.nbe342team8.domain.cart.repository.CartRepository
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.global.exceptions.ServiceException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val bookService: BookService
) {

    @Transactional
    fun updateCartItems(member: Member, cartRequestDto: CartRequestDto) {
        val cartsToSave = mutableListOf<Cart>()

        cartRequestDto.cartItems.forEach { item ->
            val book = bookService.getBookById(item.bookId)

            val cart = cartRepository.findByMemberAndBook(member, book).orElse(null)
            if (cart != null) {
                cart.updateCart(
                    if (item.isAddToCart) cart.quantity + item.quantity else item.quantity
                )
                cartsToSave.add(cart)
            } else {
                val newCart = Cart.builder()
                    .member(member)
                    .book(book)
                    .quantity(item.quantity)
                    .build()
                cartsToSave.add(newCart)
            }
        }

        cartRepository.saveAll(cartsToSave)
    }

    fun deleteProduct(member: Member, cartRequestDto: CartRequestDto) {
        cartRequestDto.cartItems.forEach { cartItemRequestDto ->
            val book = bookService.getBookById(cartItemRequestDto.bookId)
            val cartItem = cartRepository.findByMemberAndBook(member, book)
                .orElseThrow {
                    ServiceException(HttpStatus.NOT_FOUND.value(), "해당하는 장바구니 정보가 없습니다.")
                }
            cartRepository.delete(cartItem)
        }
    }

    fun deleteProduct(member: Member) {
        cartRepository.deleteByMember(member)
    }

    @Transactional(readOnly = true)
    fun findCartByMember(member: Member): List<Cart> {
        return cartRepository.findAllByMemberFetchBook(member)
    }
    @Transactional(readOnly = true)
    fun findCartDtosByMember(member: Member): List<CartResponseDto> {
        val carts = cartRepository.findAllByMemberFetchBook(member)
        return carts.map { CartResponseDto.from(it) }
    }

    fun getCartItems(@Valid cartRequestDto: CartRequestDto): List<Cart> {
        return cartRequestDto.cartItems.map { cartItemRequestDto ->
            val book = bookService.getBookById(cartItemRequestDto.bookId)
            Cart.create(book, cartItemRequestDto.quantity)
        }
    }
}
