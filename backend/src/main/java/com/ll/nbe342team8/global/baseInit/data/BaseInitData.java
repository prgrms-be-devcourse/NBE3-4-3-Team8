package com.ll.nbe342team8.global.baseInit.data;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.category.repository.CategoryRepository;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.service.ReviewService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//@Configuration
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
        List<String> coverUrls = Arrays.asList(
                "https://image.aladin.co.kr/product/4086/97/cover200/8936434128_2.jpg",
                "https://image.aladin.co.kr/product/33973/70/cover200/k682930444_1.jpg",
                "https://image.aladin.co.kr/product/29137/2/cover200/8936434594_2.jpg",
                "https://image.aladin.co.kr/product/23413/54/cover200/k072638169_1.jpg",
                "https://image.aladin.co.kr/product/27877/5/cover200/8954682154_3.jpg",
                "https://image.aladin.co.kr/product/35685/68/cover200/8997780603_1.jpg",
                "https://image.aladin.co.kr/product/35408/5/cover200/k912035329_1.jpg",
                "https://image.aladin.co.kr/product/35685/99/cover200/k312036926_2.jpg",
                "https://image.aladin.co.kr/product/32938/68/cover200/k472936042_2.jpg",
                "https://image.aladin.co.kr/product/35238/88/cover200/k132934949_1.jpg",
                "https://image.aladin.co.kr/product/35254/51/cover200/k722036927_2.jpg",
                "https://image.aladin.co.kr/product/35457/49/cover200/k802035144_1.jpg",
                "https://image.aladin.co.kr/product/33010/94/cover200/8917239501_1.jpg",
                "https://image.aladin.co.kr/product/33010/92/cover200/8917239498_1.jpg",
                "https://image.aladin.co.kr/product/30769/24/cover200/8937473402_1.jpg",
                "https://image.aladin.co.kr/product/35063/62/cover200/k532934196_2.jpg",
                "https://image.aladin.co.kr/product/35538/44/cover200/8925574101_1.jpg",
                "https://image.aladin.co.kr/product/35221/57/cover200/k172934625_1.jpg",
                "https://image.aladin.co.kr/product/35433/80/cover200/k642035736_2.jpg",
                "https://image.aladin.co.kr/product/35549/91/cover200/k752036371_2.jpg",
                "https://image.aladin.co.kr/product/35398/19/cover200/k842035019_1.jpg",
                "https://image.aladin.co.kr/product/35050/59/cover200/k262934180_1.jpg",
                "https://image.aladin.co.kr/product/35567/88/cover200/k802036377_1.jpg",
                "https://image.aladin.co.kr/product/35398/17/cover200/k432035019_1.jpg",
                "https://image.aladin.co.kr/product/2584/37/cover200/8998441012_3.jpg",
                "https://image.aladin.co.kr/product/35598/7/cover200/k632036994_1.jpg",
                "https://image.aladin.co.kr/product/14322/3/cover200/8954651135_3.jpg",
                "https://image.aladin.co.kr/product/34795/5/cover200/k782933126_2.jpg",
                "https://image.aladin.co.kr/product/17602/13/cover200/s172937955_1.jpg",
                "https://image.aladin.co.kr/product/9871/8/cover200/k042535550_2.jpg"
        );

        if (bookService.count() > 0) return;
        LocalDate date = LocalDate.of(2024, 1, 1);

        for (int i = 1; i <= 30; i++) {
            Category category = Category.builder()
                    .categoryId(0)
                    .categoryName("카테고리1")
                    .mall("국내도서")
                    .depth1("경영/경제")
                    .build();
            categoryRepository.save(category);

            Book book = Book.builder()
                    .title("제목")
                    .author("작가")
                    .priceStandard(10000)
                    .pricesSales(9000)
                    .stock(100)
                    .coverImage(coverUrls.get(i-1))
//                    .coverImage("img src")
                    .pubDate(date.plusDays(i))
                    .publisher("출판사")
                    .categoryId(category)
                    .isbn13("isbn13")
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
                reviewService.create(review, rating);
            }
        }
    }
}
