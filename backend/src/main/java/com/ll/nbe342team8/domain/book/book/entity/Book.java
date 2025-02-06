package com.ll.nbe342team8.domain.book.book.entity;

import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseTime {

    @Column(name="title" ,nullable = true,length = 100)
    private String title;      // 제목

    @Column(name = "author", nullable = true)
    private String author;     // 저자

    @Column(name = "price", nullable = true)
    private int price;         // 가격

    @Column(name = "stock", nullable = true)
    private int stock;         // 재고

    @Column(name = "rating", nullable = true)
    private float rating;      // 평점

    private String image;      // 이미지 URL

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category; // 카테고리
}
