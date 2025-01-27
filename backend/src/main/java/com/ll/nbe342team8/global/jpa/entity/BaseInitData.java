package com.ll.nbe342team8.global.jpa.entity;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.category.repository.CategoryRepository;
import com.ll.nbe342team8.domain.cart.service.CartService;
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

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final BookService bookService;
    private final CategoryRepository categoryRepository;

    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.makeSampleBooks();
        };
    }

//    @Column(name = "author", nullable = true)
//    private String author;     // 저자
//
//    @Column(name = "price", nullable = true)
//    private int price;         // 가격
//
//    @Column(name = "stock", nullable = true)
//    private int stock;         // 재고
//
//    @Column(name = "rating", nullable = true)
//    private float rating;      // 평점
//
//    private String image;      // 이미지 URL
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Category category; // 카테고리

    @Transactional
    public void makeSampleBooks() throws IOException {
        if (bookService.count() > 0) return;

        for (int i = 1; i < 30; i++) {
            Category category = Category.builder()
                    .category("Default Category")
                    .build();
            categoryRepository.save(category);
            Book book = Book.builder()
                    .author("author")
                    .price(10000)
                    .stock(100)
                    .rating(5.0f)
                    .image("img src")
                    .category(category)
                    .build();

            bookService.create(book);
        }

    }
}

