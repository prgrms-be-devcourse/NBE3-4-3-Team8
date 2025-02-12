package com.ll.nbe342team8.global.baseInit.data;

import com.ll.nbe342team8.domain.book.book.dto.ExternalBookDto;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.repository.BookRepository;
import com.ll.nbe342team8.domain.book.book.service.ExternalBookApiService;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.category.repository.CategoryRepository;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DeliveryStatus;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.domain.order.order.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class DataInitializer {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final DetailOrderRepository detailOrderRepository;
    private final ExternalBookApiService externalBookApiService;

//    @PostConstruct
    public void init() {
        // Initialize categories
        if (categoryRepository.count() == 0) {
            Category category1 = Category.builder()
                    .categoryId(1)
                    .categoryName("경제/경영")
                    .mall("국내도서")
                    .depth1("경제/경영")
                    .depth2("재테크/금융")
                    .depth3("재테크")
                    .depth4("부자되는법")
                    .depth5(null)
                    .category("국내도서 > 경제/경영 > 재테크/금융 > 재테크 > 부자되는법")
                    .books(new ArrayList<>())
                    .build();
            Category category2 = Category.builder()
                    .categoryId(2)
                    .categoryName("경제/경영")
                    .mall("국내도서")
                    .depth1("경제/경영")
                    .depth2("재테크/금융")
                    .depth3("재테크")
                    .depth4("거지 되는법")
                    .depth5(null)
                    .category("국내도서 > 경제/경영 > 재테크/금융 > 재테크 > 부자되는법")
                    .books(new ArrayList<>())
                    .build();
            categoryRepository.save(category1);
            categoryRepository.save(category2);
        }

        // Initialize members
        if (memberRepository.count() == 0) {
            Member member1 = Member.builder()
                    .name("user1")
                    .phoneNumber("010-1234-5678")
                    .memberType(Member.MemberType.USER)
                    .oAuthId("123456789L")
                    .email("chulsoo@example.com")
                    .password("securePassword1")
                    .deliveryInformations(new ArrayList<>())
                    .build();
            Member member2 = Member.builder()
                    .name("user2")
                    .phoneNumber("010-1234-5678")
                    .memberType(Member.MemberType.USER)
                    .oAuthId("123456789L")
                    .email("chulsoo11@example.com")
                    .password("securePassword2")
                    .deliveryInformations(new ArrayList<>())
                    .build();
            Member member3 = Member.builder()
                    .name("user3")
                    .phoneNumber("010-1234-5678")
                    .memberType(Member.MemberType.USER)
                    .oAuthId("123456789L")
                    .email("chulsoo3@example.com")
                    .password("securePassword3")
                    .deliveryInformations(new ArrayList<>())
                    .build();
            memberRepository.save(member1);
            memberRepository.save(member2);
            memberRepository.save(member3);
        }

        // Initialize books
        if (bookRepository.count() == 0) {
            Category category1 = categoryRepository.findById(1L).orElseThrow();
            Category category2 = categoryRepository.findById(2L).orElseThrow();
            Book book1 = Book.builder()
                    .title("부자 되는 법")
                    .author("홍길동")
                    .isbn("978-89-1234-567-8")
                    .isbn13("9788912345678")
                    .pubDate(LocalDate.of(2023, 10, 15))
                    .priceStandard(25000)
                    .pricesSales(22000)
                    .stock(100)
                    .status(1)
                    .rating(4.5)
                    .toc("1장: 시작하기\n2장: 재테크 기본기\n3장: 투자 전략")
                    .coverImage("https://example.com/cover.jpg")
                    .description("이 책은 부자가 되는 법을 알려주는 최고의 가이드입니다.")
                    .descriptionImage("https://example.com/description.jpg")
                    .salesPoint(5000L)
                    .reviewCount(120L)
                    .publisher("성공출판사")
                    .categoryId(category1)
                    .review(new ArrayList<>())
                    .build();
            Book book2 = Book.builder()
                    .title("거지 되는 법")
                    .author("홍길동")
                    .isbn("978-89-1234-567-8")
                    .isbn13("9788912345678")
                    .pubDate(LocalDate.of(2023, 10, 15))
                    .priceStandard(25000)
                    .pricesSales(22000)
                    .stock(100)
                    .status(1)
                    .rating(4.5)
                    .toc("1장: 시작하기\n2장: 재테크 기본기\n3장: 투자 전략")
                    .coverImage("https://example.com/cover.jpg")
                    .description("이 책은 부자가 되는 법을 알려주는 최고의 가이드입니다.")
                    .descriptionImage("https://example.com/description.jpg")
                    .salesPoint(5000L)
                    .reviewCount(120L)
                    .publisher("성공출판사")
                    .categoryId(category2)
                    .review(new ArrayList<>())
                    .build();

            bookRepository.save(book1);
            bookRepository.save(book2);
        }

        // Initialize orders (주문 생성)
        if (orderRepository.count() == 0) {
            Member member1 = memberRepository.findById(1L).orElseThrow();
            Member member2 = memberRepository.findById(2L).orElseThrow();
            Member member3 = memberRepository.findById(3L).orElseThrow();

            Order order1 = Order.builder()
                    .member(member1)
                    .orderStatus(Order.OrderStatus.COMPLETE)
                    .totalPrice(25000)
                    .build();
            Order order2 = Order.builder()
                    .member(member2)
                    .orderStatus(Order.OrderStatus.ORDERED)
                    .totalPrice(45000)
                    .build();
            Order order3 = Order.builder()
                    .member(member3)
                    .orderStatus(Order.OrderStatus.ORDERED)
                    .totalPrice(35000)
                    .build();

            orderRepository.save(order1);
            orderRepository.save(order2);
            orderRepository.save(order3);

            // Initialize detail orders (상세 주문 생성)
            Book book1 = bookRepository.findById(1L).orElseThrow();
            Book book2 = bookRepository.findById(2L).orElseThrow();

            DetailOrder detailOrder1 = DetailOrder.builder()
                    .order(order1)
                    .book(book1)
                    .bookQuantity(2)
                    .deliveryStatus(DeliveryStatus.PENDING)
                    .build();

            DetailOrder detailOrder2 = DetailOrder.builder()
                    .order(order2)
                    .book(book2)
                    .bookQuantity(3)
                    .deliveryStatus(DeliveryStatus.PENDING)
                    .build();

            DetailOrder detailOrder3 = DetailOrder.builder()
                    .order(order3)
                    .book(book1)
                    .bookQuantity(1)
                    .deliveryStatus(DeliveryStatus.PENDING)
                    .build();

            detailOrderRepository.save(detailOrder1);
            detailOrderRepository.save(detailOrder2);
            detailOrderRepository.save(detailOrder3);
        }
    }
}