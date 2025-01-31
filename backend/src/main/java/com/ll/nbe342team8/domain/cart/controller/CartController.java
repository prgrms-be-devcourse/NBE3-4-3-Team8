package com.ll.nbe342team8.domain.cart.controller;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.cart.dto.CartRequestDto;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.cart.service.CartService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final BookService bookService;
    private final MemberService memberService;

    @PostMapping("/{book-id}/{member-id}")
    public void addBook(@PathVariable("book-id") long bookId,
                        @PathVariable("member-id") long memberId) {

        Book book = bookService.getBookById(bookId);
        Member member = memberService.getMemberById(memberId);

        //TODO: KAKAO 로그인 연동시 member 부분 수정 필요

        Cart cart = Cart.builder()
                .member(member)
                .book(book)
                .quantity(10)
                .build();
        cartService.addProduct(cart);
    }

    @PutMapping("/{book-id}/{member-id}")
    public void updateCartItem(@PathVariable("book-id") long bookId,
                               @PathVariable("member-id") long memberId,
                               @RequestParam("quantity") int quantity) {

        Member member = memberService.getMemberById(memberId);

        Cart cartItem = member.getCart().stream()
                .filter(cart -> cart.getId().equals(bookId))
                .findFirst()
                .orElse(null);

        cartService.updateCartItem(cartItem, quantity);
    }

    @PostMapping("/{member-id}")
    public void updateCartItems(@PathVariable("member-id") long memberId,
                                @RequestBody CartRequestDto cartRequestDto){

        Member member = memberService.getMemberById(memberId);
        if (cartRequestDto != null) {
            cartService.updateCartItems(member, cartRequestDto);
        }
    }



    @DeleteMapping("/{member-id}")
    public void deleteBook(@PathVariable("member-id") long memberId,
                           @RequestBody CartRequestDto cartRequestDto) {

        Member member = memberService.getMemberById(memberId);

        if (cartRequestDto != null) {
            cartService.deleteProduct(member, cartRequestDto);
        }
    }
}
