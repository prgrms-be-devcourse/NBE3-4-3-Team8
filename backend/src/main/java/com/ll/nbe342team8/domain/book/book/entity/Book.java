package com.ll.nbe342team8.domain.book.book.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	public Long id;

	@Column(length = 100)
	@NotNull
	public String title;      // 제목

	@NotNull
	public String author; // 저자

	public String publisher; // 출판사

	public String isbn;       // ISBN

	@NotNull
	public String isbn13;     // ISBN13

	@NotNull
	public LocalDate pubDate;      //출판일

	@NotNull
	public Integer priceStandard;         // 정가

	@NotNull
	public Integer pricesSales;         // 판매가

	@NotNull
	public Integer stock;         // 재고

	@NotNull
	public Integer status;         // 판매 상태

	public Double rating = 0.0;      // 평점

	@Formula("CASE WHEN review_count = 0 THEN 0 ELSE rating / review_count END")
	public Double averageRating;        //평균 평점

	@Column(columnDefinition = "TEXT")
	public String toc;        // 목차

	public String coverImage;            // 커버 이미지 URL

	public String description;           // 상세페이지 글

	public String descriptionImage;

	public Long salesPoint;

	public Long reviewCount; // 리뷰 수

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "category_id", referencedColumnName = "id") // 외래키
	public Category categoryId; // 카테고리

	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	public List<Review> review;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DetailOrder> detailOrders = new ArrayList<>();  // 기본값 설정

	public void createReview(Double rating) {
		this.reviewCount++;
		this.rating += rating;
	}

	public void deleteReview(Double rating) {
		this.reviewCount--;
		this.rating -= rating;
	}
}
