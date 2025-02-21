package com.ll.nbe342team8.domain.book.review.service

import com.ll.nbe342team8.domain.book.book.service.BookService
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto.Companion.from
import com.ll.nbe342team8.domain.book.review.entity.Review
import com.ll.nbe342team8.domain.book.review.repository.ReviewRepository
import com.ll.nbe342team8.domain.book.review.type.ReviewSortType
import com.ll.nbe342team8.global.exceptions.ServiceException
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.function.Supplier

@Service
@RequiredArgsConstructor
class ReviewService (
    private val reviewRepository: ReviewRepository,
    private val bookService: BookService
){

    fun getAllReviews(page: Int, pageSize: Int, reviewSortType: ReviewSortType): Page<Review> {
        val sorts: MutableList<Sort.Order> = ArrayList<Sort.Order>()
        sorts.add(reviewSortType.order)

        val pageable: Pageable = PageRequest.of(page, pageSize, Sort.by(sorts))
        return reviewRepository.findAll(pageable)
    }

    fun getReviewsById(bookId: Long, page: Int, pageSize: Int, reviewSortType: ReviewSortType): Page<Review> {
        val sorts: MutableList<Sort.Order> = ArrayList<Sort.Order>()
        sorts.add(reviewSortType.order)

        val pageable: Pageable = PageRequest.of(page, pageSize, Sort.by(sorts))
        return reviewRepository.findAllByBookId(bookId, pageable)
    }

    fun getReviewById(reviewId: Long): Review {
        return reviewRepository.findById(reviewId)
            .orElseThrow<ServiceException?>(Supplier {
                ServiceException(
                    HttpStatus.NOT_FOUND.value(),
                    "id에 해당하는 리뷰가 없습니다."
                )
            })
    }

    fun create(review: Review, rating: Double?): Review {
        bookService.createReview(review.book, rating)
        return reviewRepository.save<Review>(review)
    }

    fun updateReview(reviewId: Long, content: String, rating: Double): ReviewResponseDto {
        val book = getReviewById(reviewId).book
        val review = getReviewById(reviewId)

        bookService.deleteReview(book, review.rating)
        bookService.createReview(book, rating)

        review.update(content, rating)
        val updatedReview = reviewRepository.save<Review>(review)

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
}
