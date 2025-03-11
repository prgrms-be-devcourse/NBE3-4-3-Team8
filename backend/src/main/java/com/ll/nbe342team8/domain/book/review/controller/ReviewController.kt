package com.ll.nbe342team8.domain.book.review.controller

import com.ll.nbe342team8.domain.book.book.service.BookService
import com.ll.nbe342team8.domain.book.review.dto.ReviewRequestDto
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto
import com.ll.nbe342team8.domain.book.review.entity.Review
import com.ll.nbe342team8.domain.book.review.service.ReviewService
import com.ll.nbe342team8.domain.book.review.type.ReviewSortType
import com.ll.nbe342team8.domain.member.member.service.MemberService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.hibernate.validator.constraints.Range
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "Review API")
@RequestMapping("/reviews")
class ReviewController(
    private val reviewService: ReviewService,
    private val bookService: BookService,
    private val memberService: MemberService
) {

    @GetMapping
    @Operation(summary = "전체 리뷰 조회")
    fun getAllReviews(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") @Range(min = 0, max = 100) pageSize: Int,
        @RequestParam(defaultValue = "CREATE_AT_DESC") reviewSortType: ReviewSortType
    ): Page<ReviewResponseDto> {
        val reviews = reviewService.getAllReviews(page, pageSize, reviewSortType)
        println("getAllReviews() - reviews size: ${reviews.content.size}")
        return reviews.map { ReviewResponseDto.from(it) }
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "특정 도서 리뷰 조회")
    fun getReviewsById(
        @PathVariable bookId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") @Range(min = 0, max = 100) pageSize: Int,
        @RequestParam(defaultValue = "CREATE_AT_DESC") reviewSortType: ReviewSortType
    ): Page<ReviewResponseDto> {
        val reviews = reviewService.getReviewsById(bookId, page, pageSize, reviewSortType)
        println("getReviewsById() - bookId: $bookId, reviews size: ${reviews.content.size}")
        return reviews.map { ReviewResponseDto.from(it) }
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    fun deleteReview(
        @PathVariable reviewId: Long,
        @AuthenticationPrincipal securityUser: SecurityUser?
    ): ResponseEntity<String> {
        if (securityUser == null) {
            return ResponseEntity.badRequest().body("사용자가 인증되지 않았습니다.")
        }
        val review = reviewService.getReviewById(reviewId)
        println("deleteReview() - currentUserId: ${securityUser.member.id}, reviewMemberId: ${review.member.id}")
        if (securityUser.member.id != review.member.id) {
            return ResponseEntity.badRequest().body("본인의 리뷰만 삭제할 수 있습니다.")
        }
        reviewService.deleteReview(reviewId)
        return ResponseEntity.ok("리뷰를 삭제했습니다.")
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정")
    fun updateReview(
        @PathVariable reviewId: Long,
        @RequestParam(name = "content") content: String,
        @RequestParam(name = "rating") rating: Double,
        @AuthenticationPrincipal securityUser: SecurityUser?
    ): ResponseEntity<String> {
        if (securityUser == null) {
            return ResponseEntity.badRequest().body("사용자가 인증되지 않았습니다.")
        }
        val review = reviewService.getReviewById(reviewId)
        println("updateReview() - currentUserId: ${securityUser.member.id}, reviewMemberId: ${review.member.id}")
        if (securityUser.member.id != review.member.id) {
            return ResponseEntity.badRequest().body("본인의 리뷰만 수정할 수 있습니다.")
        }
        reviewService.updateReview(reviewId, content, rating)
        return ResponseEntity.ok("리뷰를 수정했습니다.")
    }

    @PostMapping("/{book-id}")
    @Operation(summary = "리뷰 등록")
    fun createReview(
        @PathVariable("book-id") bookId: Long,
        @AuthenticationPrincipal securityUser: SecurityUser,
        @RequestBody req: ReviewRequestDto
    ) {
        val book = bookService.getBookById(bookId)
        val member = securityUser.member
        val review = Review.create(book, member, req.content, req.rating)
        reviewService.create(review, req.rating)
    }
}
