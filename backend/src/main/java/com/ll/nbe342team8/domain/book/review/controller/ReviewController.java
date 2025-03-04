package com.ll.nbe342team8.domain.book.review.controller;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.review.dto.ReviewRequestDto;
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.service.ReviewService;
import com.ll.nbe342team8.domain.book.review.type.ReviewSortType;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "Review API")
@RequestMapping("/reviews")
public class ReviewController {

    //Todo: 모든 컨트롤러 메서드가 적절한 HttpStatus 코드를 반환하도록 수정

    private final ReviewService reviewService;
    private final BookService bookService;
    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "전체 리뷰 조회")
    public Page<ReviewResponseDto> getAllReviews(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") @Range(min = 0, max = 100) int pageSize,
                                                 @RequestParam(defaultValue = "CREATE_AT_DESC") ReviewSortType reviewSortType) {
        Page<Review> reviews = reviewService.getAllReviews(page, pageSize, reviewSortType);

        return reviews.map(ReviewResponseDto::from);
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "특정 도서 리뷰 조회")
    public Page<ReviewResponseDto> getReviewsById(@PathVariable Long bookId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") @Range(min = 0, max = 100) int pageSize,
                                                  @RequestParam(defaultValue = "CREATE_AT_DESC") ReviewSortType reviewSortType) {
        Page<Review> reviews = reviewService.getReviewsById(bookId, page, pageSize, reviewSortType);

        return reviews.map(ReviewResponseDto::from);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId,
                             @AuthenticationPrincipal SecurityUser securityUser) {

        Review review = reviewService.getReviewById(reviewId);
        
        if(review.getMember().getId() != securityUser.getMember().getId()){
            return ResponseEntity.badRequest().body("본인의 리뷰만 삭제할 수 있습니다.");
        }
        
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("리뷰를 삭제했습니다.");
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId,
                             @RequestParam(name = "content") String content,
                             @RequestParam(name = "rating") Double rating,
                             @AuthenticationPrincipal SecurityUser securityUser) {

        Review review = reviewService.getReviewById(reviewId);

        if(review.getMember().getId() != securityUser.getMember().getId()){
            return ResponseEntity.badRequest().body("본인의 리뷰만 수정할 수 있습니다.");
        }
        reviewService.updateReview(reviewId, content, rating);
        return ResponseEntity.ok("리뷰를 수정했습니다.");
    }

    @PostMapping("/{book-id}")
    @Operation(summary = "리뷰 등록")
    public void createReview(@PathVariable("book-id") Long bookId,
                             @AuthenticationPrincipal SecurityUser securityUser,
                             @RequestBody ReviewRequestDto req){

        Book book = bookService.getBookById(bookId);
        Member member = securityUser.getMember();

        Review review = Review.create(book, member, req.content(), req.rating());

        reviewService.create(review, req.rating());
    }


}
