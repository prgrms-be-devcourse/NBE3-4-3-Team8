package com.ll.nbe342team8.domain.book.book.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseTime {

    @Column(length = 100)
    private String title;      // 제목

    private String author;     // 저자

    private int price;         // 가격

    private int stock;         // 재고

    private float rating;      // 평점

    private String image;      // 이미지 URL

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category; // 카테고리

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Review> review;
}
