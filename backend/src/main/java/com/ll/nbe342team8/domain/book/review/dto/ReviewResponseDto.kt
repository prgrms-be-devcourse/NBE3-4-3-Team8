package com.ll.nbe342team8.domain.book.review.dto

import com.ll.nbe342team8.domain.book.review.entity.Review
import java.time.LocalDateTime

@JvmRecord
data class ReviewResponseDto(
    val bookId: Long?,
    val reviewId: Long?,
    val memberId: Long?,
    val content: String?,
    val rating: Double?,
    val createDate: LocalDateTime?,
    val modifyDate: LocalDateTime?
) {
    companion object {
        @JvmStatic
        fun from(review: Review): ReviewResponseDto {
            return ReviewResponseDto(
                review.book.id,
                review.id,
                review.member.id,
                review.content,
                review.rating,
                review.createDate,
                review.modifyDate
            )
        }
    }
}
