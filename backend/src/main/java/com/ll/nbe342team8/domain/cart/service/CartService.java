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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public void updateCartItems(Member member, CartRequestDto cartRequestDto) {
        // 기존 장바구니 데이터를 맵으로 변환 (bookId 기준)
        Map<Long, Cart> cartMap = member.getCarts().stream()
                .collect(Collectors.toMap(cart -> cart.getBook().getId(), cart -> cart));

        // 변경된 Cart 객체를 저장할 리스트
        List<Cart> cartsToSave = new ArrayList<>();

        cartRequestDto.cartItems().forEach(item -> {
            Book book = bookService.getBookById(item.bookId());
            Cart cart = cartMap.get(book.getId());

            if (cart == null) {
                // 새로운 Cart 객체 생성
                cart = Cart.builder()
                        .member(member)
                        .book(book)
                        .quantity(item.quantity())
                        .build();
            } else {
                // 기존 Cart 객체 업데이트
                int newQuantity = item.isAddToCart() ? cart.getQuantity() + item.quantity() : item.quantity();
                cart.updateCart(newQuantity);
            }

            cartsToSave.add(cart);
        });

        // saveAll로 한 번에 저장
        cartRepository.saveAll(cartsToSave);
    }

//    public void updateCartItems(Member member, CartRequestDto cartRequestDto) {
//        cartRequestDto.cartItems().forEach(item -> {
//            Book book = bookService.getBookById(item.bookId());
//            Cart cart = findCartByBook(member, book.getId());
//
//            if (cart == null) {
//                cart = Cart.builder()
//                        .member(member)
//                        .book(book)
//                        .quantity(item.quantity())
//                        .build();
//            } else {
//                int newQuantity = item.isAddToCart() ? cart.getQuantity() + item.quantity() : item.quantity();
//                cart.updateCart(newQuantity);
//            }
//
//            cartRepository.save(cart);
//        });
//    }

    private Cart findCartByBook(Member member, Long bookId) {
        for (Cart cart : member.getCarts()) {
            if (cart.getBook().getId() == bookId) {
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

    public void deleteProduct(Member member) {
        cartRepository.deleteByMember(member);
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
