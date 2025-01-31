package com.ll.nbe342team8.domain.book.review.controller;

import com.ll.nbe342team8.domain.book.book.dto.BookResponseDto;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.repository.ReviewRepository;
import com.ll.nbe342team8.domain.book.review.service.ReviewService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
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
    private final MemberService memberService;

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

    @DeleteMapping("/{review-id}")
    public void deleteReview(@PathVariable("review-id") Long reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @PutMapping("/{review-id}")
    public void updateReview(@PathVariable("review-id") Long reviewId,
                             @RequestParam(name = "content") String content,
                             @RequestParam(name = "rating") float rating) {

        reviewService.updateReview(reviewId, content, rating);
    }

    @PostMapping("/{book-id}/{member-id}")
    public void createReview(@PathVariable("book-id") Long bookId,
                             @PathVariable("member-id") Long memberId,
                             @RequestParam(name = "content") String content,
                             @RequestParam(name = "rating") float rating){

        Book book = bookService.getBookById(bookId);
        Member member = memberService.getMemberById(memberId);

        Review review = Review.builder()
                .book(book)
                .member(member)
                .content(content)
                .rating(rating)
                .build();

        reviewService.create(review);
    }
}
