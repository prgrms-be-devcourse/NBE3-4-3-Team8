package com.ll.nbe342team8.domain.book.review.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.repository.ReviewRepository;
import com.ll.nbe342team8.domain.book.review.type.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookService bookService;

    public Page<Review> getAllReviews(int page, int pageSize, SortType sortType) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(sortType.getOrder());

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        return reviewRepository.findAll(pageable);
    }

    public Page<Review> getReviewsById(Long bookId, int page, int pageSize, SortType sortType) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(sortType.getOrder());

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        return reviewRepository.findAllByBookId(bookId, pageable);
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException());
    }

    public Review create(Review review, float rating) {
        bookService.createReview(review.getBook(), rating);
        return reviewRepository.save(review);
    }

    public ReviewResponseDto updateReview(Long reviewId, String content, float rating) {

        Book book = getReviewById(reviewId).getBook();
        Review review = getReviewById(reviewId);

        bookService.deleteReview(book, review.getRating());
        bookService.createReview(book, rating);

        review.update(content, rating);
        Review updatedReview = reviewRepository.save(review);

        return ReviewResponseDto.from(updatedReview);
    }

    public void deleteReview(Long reviewId){
        Book book = getReviewById(reviewId).getBook();
        Review review = getReviewById(reviewId);

        bookService.deleteReview(book, review.getRating());
        reviewRepository.delete(review);
    }

    public long count() {
        return reviewRepository.count();
    }
}
