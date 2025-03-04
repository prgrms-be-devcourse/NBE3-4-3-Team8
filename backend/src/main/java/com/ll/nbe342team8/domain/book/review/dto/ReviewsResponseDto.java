package com.ll.nbe342team8.domain.book.review.dto;

import com.ll.nbe342team8.domain.book.review.entity.Review;

import java.time.LocalDateTime;

public record ReviewsResponseDto(
        Long bookId,
        String bookTitle,
        String bookContent,
        Long reviewId,
        Double rating,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {

    public static ReviewsResponseDto from(Review review){
        return new ReviewsResponseDto(
                review.getBook().getId(),
                review.getBook().getTitle(),
                review.getContent(),
                review.getId(),
                review.getRating(),
                review.getCreateDate(),
                review.getModifyDate()
        );
    }
}