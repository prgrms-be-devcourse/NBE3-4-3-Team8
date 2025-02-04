package com.ll.nbe342team8.domain.book.book.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.nbe342team8.domain.book.book.dto.BookPatchRequestDto;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @Column(name = "isbn")
    private String isbn;       // ISBN

    @NotNull
    private String isbn13;     // ISBN13

    @NotNull
    private LocalDate pubDate;      //출판일

    @NotNull
    private int priceStandard;         // 정가

    @NotNull
    private int pricesSales;         // 판매가

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

    private String publisher;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "category_id") // @@@실행 이상하면 지워보기@@@
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

    public void update(BookPatchRequestDto requestDto) {
        if (requestDto.getTitle() != null) this.title = requestDto.getTitle();
        if (requestDto.getAuthor() != null) this.author = requestDto.getAuthor();
        if (requestDto.getIsbn() != null) this.isbn = requestDto.getIsbn();
        if (requestDto.getIsbn13() != null) this.isbn13 = requestDto.getIsbn13();
        if (requestDto.getPubDate() != null) this.pubDate = requestDto.getPubDate();
        if (requestDto.getPriceStandard() != null) this.priceStandard = requestDto.getPriceStandard();
        if (requestDto.getPriceSales() != null) this.pricesSales = requestDto.getPriceSales();
        if (requestDto.getStock() != null) this.stock = requestDto.getStock();
        if (requestDto.getStatus() != null) this.status = requestDto.getStatus();
        if (requestDto.getRating() != null) this.rating = requestDto.getRating();
        if (requestDto.getToc() != null) this.toc = requestDto.getToc();
        if (requestDto.getCover() != null) this.coverImage = requestDto.getCover();
        if (requestDto.getDescription() != null) this.description = requestDto.getDescription();
        if (requestDto.getDescriptionImage() != null) this.descriptionImage = requestDto.getDescriptionImage();
        if (requestDto.getCategoryId() != null) this.categoryId = requestDto.getCategoryId();
    }


}
