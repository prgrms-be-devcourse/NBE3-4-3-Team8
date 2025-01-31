package com.ll.nbe342team8.domain.book.review.dto;


import com.ll.nbe342team8.domain.book.review.entity.Review;

public record ReviewResponseDto(
        Long book,
        Long member,
        String content,
        float rating
) {

    public static ReviewResponseDto from(Review review){
        return new ReviewResponseDto(
                review.getBook().getId(),
                review.getMember().getId(),
                review.getContent(),
                review.getRating()
        );
    }
}
