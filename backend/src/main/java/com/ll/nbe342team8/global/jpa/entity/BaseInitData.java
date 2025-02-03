package com.ll.nbe342team8.global.jpa.entity;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.category.repository.CategoryRepository;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.service.ReviewService;
import com.ll.nbe342team8.domain.cart.service.CartService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.order.order.service.OrderService;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final BookService bookService;
    private final ReviewService reviewService;
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;

    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.makeSampleMembers();
            self.makeSampleBooks();
            self.makeSampleReviews();
        };
    }

    @Transactional
    public void makeSampleMembers() throws IOException {
        if (memberService.count() > 0) return;

        for (int i = 1; i <= 10; i++) {
            Member member = Member.builder()
                    .name("test" + i)
                    .phoneNumber("01012345678")
                    .memberType(Member.MemberType.USER)
                    .build();
            memberService.create(member);
        }
    }

    @Transactional
    public void makeSampleBooks() throws IOException {
        if (bookService.count() > 0) return;
        LocalDateTime date = LocalDateTime.of(2024, 12, 1, 0, 0);

        for (int i = 1; i < 30; i++) {
            Category category = Category.builder()
                    .categoryId(0)
                    .build();
            categoryRepository.save(category);

            Book book = Book.builder()
                    .title("title")
                    .author("author")
                    .price(10000)
                    .stock(100)
                    .coverImage("img src")
                    .pubDate(date.plusDays(i))
                    .rating(0.1f * i)
                    .categoryId(category)
                    .build();

            bookService.create(book);
        }

    }

    @Transactional
    // 리뷰 작성되면 Book에서 총 평점 계산
    public void makeSampleReviews() throws IOException {
        Random random = new Random();

        if (reviewService.count() > 0) return;

        for (int i = 1; i <= 10; i++) {
            Book book = bookService.getBookById((long) i);
            Member member = memberService.getMemberById((long) i);

            for (int j = 1; j <= 10; j++) {
                float rating = (float) (random.nextInt(11) * 0.5);
                Review review = Review.builder()
                        .book(book)
                        .member(member)
                        .content("review content " + j)
                        .rating(rating)
                        .build();
                reviewService.create(review);
            }
        }
    }
}
