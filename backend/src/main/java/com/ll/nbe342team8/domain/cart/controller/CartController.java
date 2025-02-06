package com.ll.nbe342team8.domain.cart.controller;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.cart.dto.CartItemRequestDto;
import com.ll.nbe342team8.domain.cart.dto.CartRequestDto;
import com.ll.nbe342team8.domain.cart.dto.CartResponseDto;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.cart.service.CartService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart API")
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final BookService bookService;
    private final MemberService memberService;

    @Operation(summary = "장바구니 추가")
    @PostMapping("/{book-id}/{member-id}")
    public void addCart(@PathVariable("book-id") long bookId,
                        @PathVariable("member-id") long memberId,
                        @RequestParam("quantity") int quantity) {

        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto(bookId, quantity);
        CartRequestDto cartRequestDto = new CartRequestDto(List.of(cartItemRequestDto));

        updateCartItems(memberId, cartRequestDto);
    }

    @Operation(summary = "장바구니 수정")
    @PutMapping("/{book-id}/{member-id}")
    public void updateCartItem(@PathVariable("book-id") long bookId,
                               @PathVariable("member-id") long memberId,
                               @RequestParam("quantity") int quantity) {

        Member member = memberService.getMemberById(memberId);

        Cart cartItem = member.getCarts().stream()
                .filter(cart -> cart.getBook().getId().equals(bookId))
                .findFirst()
                .orElse(null);

        cartService.updateCartItem(cartItem, quantity);
    }

    @Operation(summary = "장바구니 수정 json")
    @PostMapping("/{member-id}")
    public void updateCartItems(@PathVariable("member-id") long memberId,
                                @RequestBody CartRequestDto cartRequestDto){

        Member member = memberService.getMemberById(memberId);
        if (cartRequestDto != null) {
            cartService.updateCartItems(member, cartRequestDto);
        }
    }

    @Operation(summary = "장바구니 삭제")
    @DeleteMapping("/{member-id}")
    public void deleteBook(@PathVariable("member-id") long memberId,
                           @RequestBody CartRequestDto cartRequestDto) {

        Member member = memberService.getMemberById(memberId);

        if (cartRequestDto != null) {
            cartService.deleteProduct(member, cartRequestDto);
        }
    }

    @Operation(summary = "장바구니 조회")
    @GetMapping("/{member-id}")
    public List<CartResponseDto> getCart(@PathVariable("member-id") long memberId) {
        Member member = memberService.getMemberById(memberId);
        List<Cart> carts = cartService.findCartByMember(member);

        return carts.stream()
                .map(CartResponseDto::from)
                .collect(Collectors.toList());
    }
}
