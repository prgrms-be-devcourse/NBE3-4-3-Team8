package com.ll.nbe342team8.domain.book.review.dto;


import com.ll.nbe342team8.domain.book.review.entity.Review;

import java.time.LocalDateTime;

public record ReviewResponseDto(
        Long bookId,
        Long reviewId,
        Long memberId,
        String content,
        Double rating,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {

    public static ReviewResponseDto from(Review review){
        return new ReviewResponseDto(
                review.getBook().getId(),
                review.getId(),
                review.getMember().getId(),
                review.getContent(),
                review.getRating(),
                review.getCreateDate(),
                review.getModifyDate()
        );
    }
}
