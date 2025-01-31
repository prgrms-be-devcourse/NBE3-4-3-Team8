package com.ll.nbe342team8.domain.cart.dto;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.Setter;

public record CartRequestDto(
        Long member,
        Long book,
        int quantity
) {
    public static CartRequestDto to(Member member, Book book, int quantity){
        return new CartRequestDto(
                member.getId(),
                book.getId(),
                quantity
        );
    }
}
