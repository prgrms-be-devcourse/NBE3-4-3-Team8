package com.ll.nbe342team8.domain.book.review.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Review> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews;
    }

    public List<Review> getReviewsById(Long bookId) {
        List<Review> reviews = reviewRepository.findAllByBookId(bookId);
        return reviews;
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException());
    }

    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    public ReviewResponseDto updateReview(Long reviewId, String content, float rating) {
        Review review = getReviewById(reviewId);
        review.update(content, rating);
        Review updatedReview = reviewRepository.save(review);

        return ReviewResponseDto.from(updatedReview);
    }

    public void deleteReview(Long reviewId){
        Review review = getReviewById(reviewId);
        reviewRepository.delete(review);
    }

    public long count() {
        return reviewRepository.count();
    }


}
