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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "장바구니 추가")
    @PostMapping
    public void addCart(@RequestBody CartRequestDto cartRequestDto, // CartRequestDto로 변경
                        @RequestAttribute("member") Member member) {

        if (cartRequestDto != null) {
            cartService.updateCartItems(member, cartRequestDto);
        }
    }

    @Operation(summary = "장바구니 수정 json")
    @PutMapping
    public void updateCartItems(@RequestAttribute("member") Member member,
                                @RequestBody CartRequestDto cartRequestDto){

        if (cartRequestDto != null) {
            cartService.updateCartItems(member, cartRequestDto);
        }
    }

    @Operation(summary = "장바구니 삭제")
    @DeleteMapping
    public void deleteBook(@RequestAttribute("member") Member member,
                           @RequestBody CartRequestDto cartRequestDto) {

        if (cartRequestDto != null) {
            cartService.deleteProduct(member, cartRequestDto);
        }
    }

    @Operation(summary = "장바구니 조회")
    @GetMapping
    public List<CartResponseDto> getCart(@RequestAttribute("member") Member member) {
        List<Cart> carts = cartService.findCartByMember(member);

        return carts.stream()
                .map(CartResponseDto::from)
                .collect(Collectors.toList());
    }
}
