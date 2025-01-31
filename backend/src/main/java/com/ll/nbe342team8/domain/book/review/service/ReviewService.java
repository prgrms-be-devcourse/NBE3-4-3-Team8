package com.ll.nbe342team8.domain.book.review.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
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

    public long count() {
        return reviewRepository.count();
    }

    public Review create(Review review) {
        return reviewRepository.save(review);
    }
}
