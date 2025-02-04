package com.ll.nbe342team8.domain.book.book.dto;

import com.ll.nbe342team8.domain.book.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ExternalBookDto {

    private String title;             // 제목
    private String author;            // 저자
    private String publisher;         // 출판사
    private String pubDate;           // 출판일
    private String isbn;              // ISBN
    private String isbn13;            // ISBN13
    private int priceStandard;        // 정가
    private int priceSales;           // 판매가
    private String toc;               // 목차
    private String cover;             // 커버 이미지 URL
    private String description;       // 상세 설명
    private String descriptionImage;  // 상세 설명 이미지 URL
    private Category categoryId;      // 카테고리

}
