package com.ll.nbe342team8.domain.book.book.entity;

import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseTime {

    @Column(name = "title")
    private String title;      // 제목

    @Column(name = "author")
    private String author;     // 저자

    @Column(name = "isbn")
    private String isbn;       // ISBN

    @Column(name = "isbn13")
    private String isbn13;     // ISBN13

    @Column(name = "pub_date")
    private LocalDate pubDate;    // 출판일

    @Column(name = "price")
    private int price;         // 가격

    @Column(name = "stock")
    private int stock;         // 재고

    @Column(name = "status")
    private int status;         // 판매 상태 ( 1:판매중, 0:품절,절판 )

    @Column(name = "rating")
    private float rating;      // 평점

    @Column(name = "toc")
    private String toc;        // 목차

    @Column(name = "cover")
    private String cover;            // 커버 이미지 URL

    @Column(name = "description")
    private String description;           // 상세페이지 글

    @Column(name = "description_image")
    private String descriptionImage;      // 상세페이지 이미지 URL

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category; // 카테고리
    @JoinColumn(name = "category_id")
    private Category categoryId; // 카테고리

}
