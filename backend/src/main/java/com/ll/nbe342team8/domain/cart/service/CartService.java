package com.ll.nbe342team8.domain.cart.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.cart.dto.CartRequestDto;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public void addProduct(Cart cart) {
        cartRepository.save(cart);
    }

    public void updateProduct(Cart cart, int quantity) {
        cart.updateCart(quantity);
        cartRepository.save(cart);
    }
}
