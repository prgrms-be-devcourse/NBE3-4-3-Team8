package com.ll.nbe342team8.domain.book.review.dto;

import com.ll.nbe342team8.domain.book.review.entity.Review;

import java.time.LocalDateTime;

public record ReviewRequestDto(
        String content,
        Double rating
) {

    public static ReviewRequestDto from(Review review){
        return new ReviewRequestDto(
                review.getContent(),
                review.getRating()
        );
    }
}

