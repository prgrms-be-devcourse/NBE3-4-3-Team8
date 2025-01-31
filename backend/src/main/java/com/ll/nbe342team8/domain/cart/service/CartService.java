package com.ll.nbe342team8.domain.cart.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.cart.dto.CartItemRequestDto;
import com.ll.nbe342team8.domain.cart.dto.CartRequestDto;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.cart.repository.CartRepository;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookService bookService;

    public void addProduct(Cart cart) {
        cartRepository.save(cart);
    }

    public void updateProduct(Cart cart, int quantity) {
        cart.updateCart(quantity);
        cartRepository.save(cart);
    }

    public void deleteProduct(Member member, CartRequestDto cartRequestDto) {

        cartRequestDto.cartItems()
                .forEach(cartItemRequestDto -> {
                    Cart cartItem = member.getCart().stream()
                            .filter(cart -> cart.getBook().getId().equals(cartItemRequestDto.bookId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("장바구니에 없음"));
                    cartRepository.delete(cartItem);
                });
    }
}
