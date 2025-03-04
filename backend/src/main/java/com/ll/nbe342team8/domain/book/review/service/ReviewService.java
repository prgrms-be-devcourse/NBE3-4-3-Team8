package com.ll.nbe342team8.domain.book.review.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.review.dto.ReviewResponseDto;
import com.ll.nbe342team8.domain.book.review.dto.ReviewsResponseDto;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.repository.ReviewRepository;
import com.ll.nbe342team8.domain.book.review.type.ReviewSortType;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.question.dto.QuestionDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import com.ll.nbe342team8.standard.PageDto.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookService bookService;

    public Page<Review> getAllReviews(int page, int pageSize, ReviewSortType reviewSortType) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(reviewSortType.getOrder());

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        return reviewRepository.findAll(pageable);
    }

    public Page<Review> getReviewsById(Long bookId, int page, int pageSize, ReviewSortType reviewSortType) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(reviewSortType.getOrder());

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        return reviewRepository.findAllByBookId(bookId, pageable);
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "id에 해당하는 리뷰가 없습니다."));
    }

    public Review create(Review review, Double rating) {
        bookService.createReview(review.getBook(), rating);
        return reviewRepository.save(review);
    }

    public ReviewResponseDto updateReview(Long reviewId, String content, Double rating) {

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

    public PageDto<ReviewsResponseDto> getMemberReviewPage(Member member, int page) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        Page<Review> paging = this.reviewRepository.findByMember(pageable, member);

        Page<ReviewsResponseDto> pagingOrderDto = paging.map(ReviewsResponseDto::from);
        PageDto<ReviewsResponseDto> pageDto = new PageDto<>(pagingOrderDto);
        return pageDto;

    }
}
