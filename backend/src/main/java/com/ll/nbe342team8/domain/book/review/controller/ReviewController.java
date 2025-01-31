package com.ll.nbe342team8.domain.book.review.controller;

import com.ll.nbe342team8.domain.book.book.dto.BookResponseDto;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final BookService bookService;

    // 전체 리뷰 조회
    @GetMapping
    public List<ReviewResponseDto> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }
    // 특정 도서 리뷰 조회
    @GetMapping("/{book-id}")
    public List<ReviewResponseDto> getReviewsById(@PathVariable("book-id") Long bookId) {
        List<Review> reviews = reviewService.getReviewsById(bookId);

        return reviews.stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }
}
