package com.ll.nbe342team8.domain.book.review.dto;


import com.ll.nbe342team8.domain.book.review.entity.Review;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        Long bookId,
        Long reviewId,
        String author,
        String content,
        float rating,
        LocalDateTime createDate
) {

    public static ReviewResponseDto from(Review review){
        return new ReviewResponseDto(
                review.getBook().getId(),
                review.getId(),
                review.getMember().getName(),
                review.getContent(),
                review.getRating(),
                review.getCreateDate()
        );
    }
}
