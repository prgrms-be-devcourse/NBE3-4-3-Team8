package com.ll.nbe342team8.domain.book.book.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseTime {

    @Column(length = 100)
    @NotNull
    private String title;      // 제목

    @NotNull
    private String author;     // 저자

    @NotNull
    private String isbn13;     // ISBN13

    @NotNull
    private LocalDateTime pubDate;      //출판일

    @NotNull
    private int price;         // 가격

    @NotNull
    private int stock;         // 재고

    @NotNull
    private int status;         // 판매 상태

    private float rating;      // 평점

    private String toc;        // 목차

    private String coverImage;            // 커버 이미지 URL

    private String description;           // 상세페이지 글

    @Column(name = "description2")
    private String descriptionImage;

    private long salesPoint;

    private long reviewCount;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Category categoryId; // 카테고리

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Review> review;

    public void createReview(float rating) {
        this.reviewCount++;
        this.rating += rating;
    }

    public void deleteReview(float rating) {
        this.reviewCount--;
        this.rating -= rating;
    }
}
