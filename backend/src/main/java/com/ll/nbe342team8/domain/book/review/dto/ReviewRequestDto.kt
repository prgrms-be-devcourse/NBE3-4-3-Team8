package com.ll.nbe342team8.domain.book.review.dto

import com.ll.nbe342team8.domain.book.review.entity.Review

@JvmRecord
data class ReviewRequestDto(
    val content: String,
    val rating: Double
) {
    companion object {
        fun from(review: Review): ReviewRequestDto {
            return ReviewRequestDto(
                review.content,
                review.rating
            )
        }
    }
}

