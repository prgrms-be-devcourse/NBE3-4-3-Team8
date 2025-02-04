package com.ll.nbe342team8.domain.book.review.controller;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.service.ReviewService;
import com.ll.nbe342team8.domain.book.review.type.SortType;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "Review API")
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final BookService bookService;
    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "전체 리뷰 조회")
    public Page<ReviewResponseDto> getAllReviews(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @RequestParam(defaultValue = "CREATE_AT_DESC") SortType sortType) {
        Page<Review> reviews = reviewService.getAllReviews(page, pageSize, sortType);

        return reviews.map(ReviewResponseDto::from);
    }

    @GetMapping("/{book-id}")
    @Operation(summary = "특정 도서 리뷰 조회")
    public Page<ReviewResponseDto> getReviewsById(@PathVariable("book-id") Long bookId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @RequestParam(defaultValue = "CREATE_AT_DESC") SortType sortType) {
        Page<Review> reviews = reviewService.getReviewsById(bookId, page, pageSize, sortType);

        return reviews.map(ReviewResponseDto::from);
    }

    @DeleteMapping("/{review-id}")
    @Operation(summary = "리뷰 삭제")
    public void deleteReview(@PathVariable("review-id") Long reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @PutMapping("/{review-id}")
    @Operation(summary = "리뷰 수정")
    public void updateReview(@PathVariable("review-id") Long reviewId,
                             @RequestParam(name = "content") String content,
                             @RequestParam(name = "rating") float rating) {

        reviewService.updateReview(reviewId, content, rating);
    }

    @PostMapping("/{book-id}/{member-id}")
    @Operation(summary = "리뷰 등록")
    public void createReview(@PathVariable("book-id") Long bookId,
                             @PathVariable("member-id") Long memberId,
                             @RequestBody Review req){

        Book book = bookService.getBookById(bookId);
        Member member = memberService.getMemberById(memberId);

        Review review = Review.builder()
                .book(book)
                .member(member)
                .content(req.getContent())
                .rating(req.getRating())
                .build();

        reviewService.create(review, req.getRating());
    }
}
