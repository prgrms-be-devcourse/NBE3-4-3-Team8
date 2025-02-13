package com.ll.nbe342team8.global.initData;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.repository.BookRepository;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.category.repository.CategoryRepository;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.domain.order.order.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

//@RequiredArgsConstructor
//@Component
//public class DataInitializer {
//
//    private final CategoryRepository categoryRepository;
//    private final MemberRepository memberRepository;
//    private final BookRepository bookRepository;
//    private final OrderRepository orderRepository;
//    private final DetailOrderRepository detailOrderRepository;
//
//    @PostConstruct
//    public void init() {
//        // 카테고리 데이터 초기화
//        if (categoryRepository.count() == 0) {
//            Category category1 = Category.builder()
//                    .categoryId(1)
//                    .categoryName("경제/경영")
//                    .mall("국내도서")
//                    .depth1("경제/경영")
//                    .depth2("재테크/금융")
//                    .depth3("재테크")
//                    .depth4("부자되는법")
//                    .depth5(null) // 값이 없는 경우 null
//                    .category("국내도서 > 경제/경영 > 재테크/금융 > 재테크 > 부자되는법")
//                    .books(new ArrayList<>()) // 초기에는 빈 리스트
//                    .build();
//            Category category2= Category.builder()
//                    .categoryId(2)
//                    .categoryName("경제/경영")
//                    .mall("국내도서")
//                    .depth1("경제/경영")
//                    .depth2("재테크/금융")
//                    .depth3("재테크")
//                    .depth4("거지 되는법")
//                    .depth5(null) // 값이 없는 경우 null
//                    .category("국내도서 > 경제/경영 > 재테크/금융 > 재테크 > 부자되는법")
//                    .books(new ArrayList<>()) // 초기에는 빈 리스트
//                    .build();
//            categoryRepository.save(category1);
//            categoryRepository.save(category2);
//        }
//
//        // 회원 데이터 초기화
//        if (memberRepository.count() == 0) {
//            Member member1 = Member.builder()
//                    .name("user1")
//                    .phoneNumber("010-1234-5678")
//                    .memberType(Member.MemberType.USER) // 사용자 역할
//                    .oAuthId("123456789") // 예제 OAuth ID
//                    .email("chulsoo@example.com") // 소셜 로그인 ID
//                    .deliveryInformations(new ArrayList<>()) // 초기에는 빈 리스트
//                    .build();
//            Member member2 = Member.builder()
//                    .name("user2")
//                    .phoneNumber("010-1234-5678")
//                    .memberType(Member.MemberType.USER) // 사용자 역할
//                    .oAuthId("123456789") // 예제 OAuth ID
//                    .email("chulsoo11@example.com") // 소셜 로그인 ID
//                    .deliveryInformations(new ArrayList<>()) // 초기에는 빈 리스트
//                    .build();
//            Member member3 = Member.builder()
//                    .name("user3")
//                    .phoneNumber("010-1234-5678")
//                    .memberType(Member.MemberType.USER) // 사용자 역할
//                    .oAuthId("123456789") // 예제 OAuth ID
//                    .email("chulsoo3@example.com") // 소셜 로그인 ID
//                    .deliveryInformations(new ArrayList<>()) // 초기에는 빈 리스트
//                    .build();
//            memberRepository.save(member1);
//            memberRepository.save(member2);
//            memberRepository.save(member3);
//        }
//
//        // 상품 데이터 초기화
//        if (bookRepository.count() == 0) {
//            Category category1 = categoryRepository.findById(1L).orElseThrow();  // 카테고리 데이터 가져오기
//            Category category2 = categoryRepository.findById(2L).orElseThrow();
//
//            Book book1 = Book.builder()
//                    .title("부자 되는 법") // 책 제목
//                    .memberId("홍길동") // 저자
//                    .isbn("978-89-1234-567-8") // ISBN
//                    .isbn13("9788912345678") // ISBN13
//                    .pubDate(LocalDate.of(2023, 10, 15)) // 출판일
//                    .priceStandard(25000) // 정가
//                    .pricesSales(22000) // 판매가
//                    .stock(100) // 재고
//                    .status(1) // 판매 상태 (1: 판매 중, 0: 품절 등)
//                    .rating(4.5) // 평점
//                    .toc("1장: 시작하기\n2장: 재테크 기본기\n3장: 투자 전략") // 목차
//                    .coverImage("https://example.com/cover.jpg") // 커버 이미지 URL
//                    .description("이 책은 부자가 되는 법을 알려주는 최고의 가이드입니다.") // 상세 설명
//                    .descriptionImage("https://example.com/description.jpg") // 상세 이미지 URL
//                    .salesPoint(5000) // 판매 포인트
//                    .reviewCount(120) // 리뷰 개수
//                    .publisher("성공출판사") // 출판사
//                    .categoryId(category1) // 앞서 생성한 Category 객체를 사용
//                    .review(new ArrayList<>()) // 초기에는 빈 리스트
//                    .build();
//            Book book2 = Book.builder()
//                    .title("거지 되는 법") // 책 제목
//                    .memberId("홍길동") // 저자
//                    .isbn("978-89-1234-567-8") // ISBN
//                    .isbn13("9788912345678") // ISBN13
//                    .pubDate(LocalDate.of(2023, 10, 15)) // 출판일
//                    .priceStandard(25000) // 정가
//                    .pricesSales(22000) // 판매가
//                    .stock(100) // 재고
//                    .status(1) // 판매 상태 (1: 판매 중, 0: 품절 등)
//                    .rating(4.5f) // 평점
//                    .toc("1장: 시작하기\n2장: 재테크 기본기\n3장: 투자 전략") // 목차
//                    .coverImage("https://example.com/cover.jpg") // 커버 이미지 URL
//                    .description("이 책은 부자가 되는 법을 알려주는 최고의 가이드입니다.") // 상세 설명
//                    .descriptionImage("https://example.com/description.jpg") // 상세 이미지 URL
//                    .salesPoint(5000) // 판매 포인트
//                    .reviewCount(120) // 리뷰 개수
//                    .publisher("성공출판사") // 출판사
//                    .categoryId(category2) // 앞서 생성한 Category 객체를 사용
//                    .review(new ArrayList<>()) // 초기에는 빈 리스트
//                    .build();
//
//            bookRepository.save(book1);
//            bookRepository.save(book2);
//        }
//
//        // 주문 데이터 초기화
//        if (orderRepository.count() == 0) {
//            Member member1 = memberRepository.findById(1L).orElseThrow();  // 이미 초기화된 회원을 가져옴
//            Member member2 = memberRepository.findById(2L).orElseThrow();
//            Member member3 = memberRepository.findById(3L).orElseThrow();
//
//            Order order1 = new Order(member1, Order.OrderStatus.COMPLETE, 2500);
//            Order order2 = new Order(member2, Order.OrderStatus.ORDERED, 4500);
//            Order order3 = new Order(member3, Order.OrderStatus.ORDERED, 3500);
//            orderRepository.save(order1);
//            orderRepository.save(order2);
//            orderRepository.save(order3);
//
//            // 주문 세부 사항 (DetailOrder)
//            Book book1 = bookRepository.findById(1L).orElseThrow();  // 이미 초기화된 상품을 가져옴
//            Book book2 = bookRepository.findById(2L).orElseThrow();
//            Book book3 = bookRepository.findById(3L).orElseThrow();
//
//            DetailOrder detailOrder1 = DetailOrder.builder()
//                    .order(order1)
//                    .book(book1)
//                    .bookQuantity(2)
//                    .deliveryStatus(DetailOrder.DeliveryStatus.PENDING)
//                    .build();
//
//            DetailOrder detailOrder2 = DetailOrder.builder()
//                    .order(order2)
//                    .book(book2)
//                    .bookQuantity(2)
//                    .deliveryStatus(DetailOrder.DeliveryStatus.PENDING)
//                    .build();
//
//            DetailOrder detailOrder3  = DetailOrder.builder()
//                    .order(order3)
//                    .book(book3)
//                    .bookQuantity(3)
//                    .deliveryStatus(DetailOrder.DeliveryStatus.PENDING)
//                    .build();
//
//            detailOrderRepository.save(detailOrder1);
//            detailOrderRepository.save(detailOrder2);
//            detailOrderRepository.save(detailOrder3);
//        }
//    }
//}