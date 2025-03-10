package com.ll.nbe342team8.Book.Review;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.service.ReviewService;
import com.ll.nbe342team8.domain.book.review.type.ReviewSortType;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    // 테스트용 SecurityUser 빈 주입 (TestSecurityConfig에서 생성한 빈)
    @Autowired
    private SecurityUser testSecurityUser;

    @Test
    @DisplayName("전체 리뷰 조회 테스트")
    void testGetAllReviews() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/reviews")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .param("reviewSortType", ReviewSortType.CREATE_AT_DESC.name())
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        Page<Review> reviewPage = reviewService.getAllReviews(0, 10, ReviewSortType.CREATE_AT_DESC);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(reviewPage.getTotalElements()));
    }

    @Test
    @DisplayName("특정 도서 리뷰 조회 테스트")
    void testGetReviewsByBookId() throws Exception {
        Book book = bookService.getAllBooks(0, 1,
                        com.ll.nbe342team8.domain.book.book.type.BookSortType.PUBLISHED_DATE)
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트용 책이 없습니다."));
        Long bookId = book.getId();

        ResultActions resultActions = mockMvc.perform(get("/reviews/" + bookId)
                        .param("page", "0")
                        .param("pageSize", "10")
                        .param("reviewSortType", ReviewSortType.CREATE_AT_DESC.name())
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        Page<Review> reviewPage = reviewService.getReviewsById(bookId, 0, 10, ReviewSortType.CREATE_AT_DESC);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(reviewPage.getTotalElements()));
    }

    @Test
    @DisplayName("리뷰 등록 테스트")
    void testCreateReview() throws Exception {
        Book book = bookService.getAllBooks(0, 1,
                        com.ll.nbe342team8.domain.book.book.type.BookSortType.PUBLISHED_DATE)
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트용 책이 없습니다."));
        Long bookId = book.getId();

        String reviewJson = """
                {
                  "content": "테스트 리뷰 내용",
                  "rating": 4.5
                }
                """;

        ResultActions resultActions = mockMvc.perform(post("/reviews/" + bookId)
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andDo(print());

        resultActions.andExpect(status().isOk());

        Page<Review> reviewsAfter = reviewService.getReviewsById(bookId, 0, 10, ReviewSortType.CREATE_AT_DESC);
        assertThat(reviewsAfter.getContent())
                .anyMatch(r -> "테스트 리뷰 내용".equals(r.getContent()) && r.getRating() == 4.5);
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    void testUpdateReview() throws Exception {
        // 먼저 테스트용 책을 가져온 후, 테스트용 리뷰를 새로 등록합니다.
        Book book = bookService.getAllBooks(0, 1,
                        com.ll.nbe342team8.domain.book.book.type.BookSortType.PUBLISHED_DATE)
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트용 책이 없습니다."));
        Long bookId = book.getId();

        String createReviewJson = """
                {
                  "content": "수정 전 리뷰 내용",
                  "rating": 5.0
                }
                """;

        // POST를 통해 리뷰를 생성 (이 리뷰는 testSecurityUser 소유로 생성됩니다.)
        mockMvc.perform(post("/reviews/" + bookId)
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createReviewJson))
                .andExpect(status().isOk());

        // 생성된 리뷰 중 "수정 전 리뷰 내용"을 가진 리뷰를 조회합니다.
        Page<Review> reviews = reviewService.getReviewsById(bookId, 0, 10, ReviewSortType.CREATE_AT_DESC);
        Review review = reviews.getContent().stream()
                .filter(r -> "수정 전 리뷰 내용".equals(r.getContent()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("생성한 리뷰를 찾을 수 없습니다."));
        Long reviewId = review.getId();

        // 해당 리뷰를 수정합니다.
        ResultActions resultActions = mockMvc.perform(put("/reviews/" + reviewId)
                        .param("content", "수정된 리뷰 내용")
                        .param("rating", "3.0")
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("리뷰를 수정했습니다."));

        Review updatedReview = reviewService.getReviewById(reviewId);
        assertThat(updatedReview.getContent()).isEqualTo("수정된 리뷰 내용");
        assertThat(updatedReview.getRating()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void testDeleteReview() throws Exception {
        // 먼저 테스트용 책을 가져오고, 테스트용 리뷰를 새로 등록합니다.
        Book book = bookService.getAllBooks(0, 1,
                        com.ll.nbe342team8.domain.book.book.type.BookSortType.PUBLISHED_DATE)
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트용 책이 없습니다."));
        Long bookId = book.getId();

        String createReviewJson = """
                {
                  "content": "삭제할 리뷰 내용",
                  "rating": 4.0
                }
                """;

        // POST를 통해 리뷰를 생성 (이 리뷰는 testSecurityUser 소유로 생성됩니다.)
        mockMvc.perform(post("/reviews/" + bookId)
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createReviewJson))
                .andExpect(status().isOk());

        // 생성된 리뷰 중 "삭제할 리뷰 내용"을 가진 리뷰를 조회합니다.
        Page<Review> reviews = reviewService.getReviewsById(bookId, 0, 10, ReviewSortType.CREATE_AT_DESC);
        Review review = reviews.getContent().stream()
                .filter(r -> "삭제할 리뷰 내용".equals(r.getContent()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("생성한 리뷰를 찾을 수 없습니다."));
        Long reviewId = review.getId();

        // 해당 리뷰를 삭제합니다.
        ResultActions resultActions = mockMvc.perform(delete("/reviews/" + reviewId)
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser)))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("리뷰를 삭제했습니다."));

        Page<Review> afterPage = reviewService.getAllReviews(0, 10, ReviewSortType.CREATE_AT_DESC);
        assertThat(afterPage.getContent()).noneMatch(r -> r.getId().equals(reviewId));
    }
}
