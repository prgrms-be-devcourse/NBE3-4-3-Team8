package com.ll.nbe342team8.domain.cart.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.cart.dto.CartItemRequestDto;
import com.ll.nbe342team8.domain.cart.dto.CartRequestDto;
import com.ll.nbe342team8.domain.cart.dto.CartResponseDto;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.cart.repository.CartRepository;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookService bookService;

    public void addProduct(Cart cart) {
        cartRepository.save(cart);
    }

    public void updateCartItem(Cart cart, int quantity) {
        cart.updateCart(quantity);
        cartRepository.save(cart);
    }

    public void updateCartItems(Member member, CartRequestDto cartRequestDto) {
        cartRequestDto.cartItems().forEach(item -> {
            Book book = bookService.getBookById(item.bookId());
            Cart cart = findCartByBook(member, item.bookId());

            if (cart == null) {
                cart = Cart.builder()
                        .member(member)
                        .book(book)
                        .quantity(item.quantity())
                        .build();
            } else {
                int newQuantity = item.isAddToCart() ? cart.getQuantity() + item.quantity() : item.quantity();
                cart.updateCart(newQuantity);
            }

            cartRepository.save(cart);
        });
    }



    private Cart findCartByBook(Member member, Long bookId) {
        for (Cart cart : member.getCarts()) {
            if (cart.getBook().getId().equals(bookId)) {
                return cart;
            }
        }
        return null;
    }

    public void deleteProduct(Member member, CartRequestDto cartRequestDto) {

        cartRequestDto.cartItems()
                .forEach(cartItemRequestDto -> {
                    Cart cartItem = member.getCarts().stream()
                            .filter(cart -> cart.getBook().getId().equals(cartItemRequestDto.bookId()))
                            .findFirst()
                            .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "해당하는 장바구니 정보가 없습니다."));
                    cartRepository.delete(cartItem);
                });
    }

    public List<Cart> findCartByMember(Member member) {
        return cartRepository.findAllByMember(member);
    }

    public List<Cart> getCartItems(@Valid CartRequestDto cartRequestDto) {
        return cartRequestDto.cartItems().stream()
                .map(cartItemRequestDto -> {
                    Book book = bookService.getBookById(cartItemRequestDto.bookId());
                    return Cart.create(book, cartItemRequestDto.quantity());
                })
                .toList();
    }
}
