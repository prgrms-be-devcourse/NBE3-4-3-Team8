package com.ll.nbe342team8.domain.book.review.service

import com.ll.nbe342team8.domain.book.book.service.BookService
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto.Companion.from
import com.ll.nbe342team8.domain.book.review.dto.ReviewsResponseDto
import com.ll.nbe342team8.domain.book.review.entity.Review
import com.ll.nbe342team8.domain.book.review.repository.ReviewRepository
import com.ll.nbe342team8.domain.book.review.type.ReviewSortType
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.global.exceptions.ServiceException
import com.ll.nbe342team8.standard.PageDto.PageDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ReviewService (
    private val reviewRepository: ReviewRepository,
    private val bookService: BookService
){

    fun getAllReviews(page: Int, pageSize: Int, reviewSortType: ReviewSortType): Page<Review> {
        val sorts = ArrayList<Sort.Order>()
        sorts.add(reviewSortType.order)

        val pageable: Pageable = PageRequest.of(page, pageSize, Sort.by(sorts))
        return reviewRepository.findAll(pageable)
    }

    fun getReviewsById(bookId: Long, page: Int, pageSize: Int, reviewSortType: ReviewSortType): Page<Review> {
        val sorts = ArrayList<Sort.Order>()
        sorts.add(reviewSortType.order)

        val pageable: Pageable = PageRequest.of(page, pageSize, Sort.by(sorts))
        return reviewRepository.findAllByBookId(bookId, pageable)
    }

    fun getReviewById(reviewId: Long): Review {
        return reviewRepository.findById(reviewId)
            .orElseThrow {
                ServiceException(
                    HttpStatus.NOT_FOUND.value(),
                    "id에 해당하는 리뷰가 없습니다."
                )
            }
    }

    fun create(review: Review, rating: Double?): Review {
        bookService.createReview(review.book, rating)
        return reviewRepository.save(review)
    }

    fun updateReview(reviewId: Long, content: String, rating: Double): ReviewResponseDto {
        val book = getReviewById(reviewId).book
        val review = getReviewById(reviewId)

        bookService.deleteReview(book, review.rating)
        bookService.createReview(book, rating)

        review.update(content, rating)
        val updatedReview = reviewRepository.save(review)

        return from(updatedReview)
    }

    fun deleteReview(reviewId: Long) {
        val book = getReviewById(reviewId).book
        val review = getReviewById(reviewId)

        bookService.deleteReview(book, review.rating)
        reviewRepository.delete(review)
    }

    fun count(): Long {
        return reviewRepository.count()
    }

    fun getMemberReviewPage(member: Member, page: Int): PageDto<ReviewsResponseDto> {
        val pageable = PageRequest.of(page, 10, Sort.by("createDate").descending())
        val paging = reviewRepository.findByMember(pageable, member)

        val pagingOrderDto = paging?.map { ReviewsResponseDto.from(it) }
        return PageDto(pagingOrderDto)
    }
}
