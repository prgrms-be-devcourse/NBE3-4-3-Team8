package com.ll.nbe342team8.domain.order.data;

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

@RequiredArgsConstructor
@Component
public class DataInitializer {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final DetailOrderRepository detailOrderRepository;


    @PostConstruct
    public void init() {
        // 카테고리 데이터 초기화
        if (categoryRepository.count() == 0) {
            Category category1 = new Category("Fiction");
            Category category2 = new Category("Non-Fiction");
            categoryRepository.save(category1);
            categoryRepository.save(category2);
        }

        // 회원 데이터 초기화
        if (memberRepository.count() == 0) {
            Member member1 = new Member("user1", "01012345678", Member.MemberType.USER, 123L);
            Member member2 = new Member("user2", "01023456789", Member.MemberType.USER, 456L);
            Member member3 = new Member("user3", "01034567890", Member.MemberType.USER, 789L);
            memberRepository.save(member1);
            memberRepository.save(member2);
            memberRepository.save(member3);
        }

        // 상품 데이터 초기화
        if (bookRepository.count() == 0) {
            Category category1 = categoryRepository.findById(1L).orElseThrow();  // 카테고리 데이터 가져오기
            Category category2 = categoryRepository.findById(2L).orElseThrow();

            Book book1 = new Book("Book 1", "Author 1", 1000, 50, 4.5f, "Description 1", category1);
            Book book2 = new Book("Book 2", "Author 2", 1500, 30, 4.0f, "Description 2", category2);

            bookRepository.save(book1);
            bookRepository.save(book2);
        }
// 주문 데이터 초기화
        if (orderRepository.count() == 0) {
            Member member1 = memberRepository.findById(1L).orElseThrow();  // 이미 초기화된 회원을 가져옴
            Member member2 = memberRepository.findById(2L).orElseThrow();
            Member member3 = memberRepository.findById(3L).orElseThrow();

            // 기존 주문
            Order order1 = new Order(member1, Order.OrderStatus.COMPLETE, 2500);
            Order order2 = new Order(member2, Order.OrderStatus.ORDERED, 4500);
            Order order3 = new Order(member3, Order.OrderStatus.ORDERED, 3500);
            orderRepository.save(order1);
            orderRepository.save(order2);
            orderRepository.save(order3);

            // member2에게 3개의 주문 추가
            Order order4 = new Order(member2, Order.OrderStatus.ORDERED, 5000);
            Order order5 = new Order(member2, Order.OrderStatus.CANCELLED, 3200);
            Order order6 = new Order(member2, Order.OrderStatus.COMPLETE, 1500);

            orderRepository.save(order4);
            orderRepository.save(order5);
            orderRepository.save(order6);

            // 주문 세부 사항 (DetailOrder)
            Book book1 = bookRepository.findById(1L).orElseThrow();  // 이미 초기화된 상품을 가져옴
            Book book2 = bookRepository.findById(2L).orElseThrow();

            // 기존 주문에 DetailOrder 추가
            DetailOrder detailOrder1 = new DetailOrder(order1, book1, 2, DetailOrder.DeliveryStatus.PENDING);
            DetailOrder detailOrder2 = new DetailOrder(order2, book2, 3, DetailOrder.DeliveryStatus.RETURNED);
            DetailOrder detailOrder3 = new DetailOrder(order3, book1, 1, DetailOrder.DeliveryStatus. SHIPPING);

            // member2의 추가된 주문에 DetailOrder 추가
            DetailOrder detailOrder4 = new DetailOrder(order4, book2, 2, DetailOrder.DeliveryStatus.PENDING);
            DetailOrder detailOrder5 = new DetailOrder(order5, book1, 1, DetailOrder.DeliveryStatus.SHIPPING);
            DetailOrder detailOrder6 = new DetailOrder(order6, book2, 2, DetailOrder.DeliveryStatus.DELIVERED);

            detailOrderRepository.save(detailOrder1);
            detailOrderRepository.save(detailOrder2);
            detailOrderRepository.save(detailOrder3);
            detailOrderRepository.save(detailOrder4);
            detailOrderRepository.save(detailOrder5);
            detailOrderRepository.save(detailOrder6);
        }
    }
}