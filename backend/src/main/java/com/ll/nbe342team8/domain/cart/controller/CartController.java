package com.ll.nbe342team8.domain.cart.controller;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.cart.dto.CartItemRequestDto;
import com.ll.nbe342team8.domain.cart.dto.CartRequestDto;
import com.ll.nbe342team8.domain.cart.dto.CartResponseDto;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.cart.service.CartService;
import com.ll.nbe342team8.domain.jwt.AuthService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart API")
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final BookService bookService;

    @Operation(summary = "장바구니 추가")
    @PostMapping
    public void addCart(@RequestBody CartRequestDto cartRequestDto, // CartRequestDto로 변경
                        @AuthenticationPrincipal SecurityUser securityUser) {

        Member member = securityUser.getMember();

        if (cartRequestDto != null) {
            cartService.updateCartItems(member, cartRequestDto);
        }
    }

    @Operation(summary = "장바구니 수정 json")
    @PutMapping
    public void updateCartItems(@AuthenticationPrincipal SecurityUser securityUser,
                                @RequestBody @Valid CartRequestDto cartRequestDto) {

        Member member = securityUser.getMember();

        if (cartRequestDto != null) {
            cartService.updateCartItems(member, cartRequestDto);
        }
    }

    @Operation(summary = "장바구니 삭제")
    @DeleteMapping
    public void deleteBook(@AuthenticationPrincipal SecurityUser securityUser,
                           @RequestBody CartRequestDto cartRequestDto) {

        Member member = securityUser.getMember();

        if (cartRequestDto != null) {
            cartService.deleteProduct(member, cartRequestDto);
        }
    }

    @Operation(summary = "장바구니 조회")
    @GetMapping
    public List<CartResponseDto> getCart(@AuthenticationPrincipal SecurityUser securityUser) {

        Member member = securityUser.getMember();
        List<Cart> carts = cartService.findCartByMember(member);

        return carts.stream()
                .map(CartResponseDto::from)
                .collect(Collectors.toList());
    }

    @PostMapping("/anonymous")
    public ResponseEntity<List<CartResponseDto>> getAnonymousCart(@RequestBody @Valid CartRequestDto cartRequestDto) {

        List<Cart> cartItems = cartService.getCartItems(cartRequestDto);
        List<CartResponseDto> cartResponseDto = cartItems.stream()
                .map(CartResponseDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(cartResponseDto);
    }
}
