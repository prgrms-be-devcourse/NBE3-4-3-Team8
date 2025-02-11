package com.ll.nbe342team8.domain.book.book.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	private Long id;

	@Column(length = 100)
	@NotNull
	private String title;      // 제목

	@NotNull
	private String author; // 저자

	private String publisher; // 출판사

	private String isbn;       // ISBN

	@NotNull
	private String isbn13;     // ISBN13

	@NotNull
	private LocalDate pubDate;      //출판일

	@NotNull
	private Integer priceStandard;         // 정가

	@NotNull
	private Integer pricesSales;         // 판매가

	@NotNull
	private Integer stock;         // 재고

	@NotNull
	private Integer status;         // 판매 상태

	private Double rating = 0.0;      // 평점

	@Formula("CASE WHEN review_count = 0 THEN 0 ELSE rating / review_count END")
	private Double averageRating;        //평균 평점

	@Column(columnDefinition = "TEXT")
	private String toc;        // 목차

	private String coverImage;            // 커버 이미지 URL

	private String description;           // 상세페이지 글

	private String descriptionImage;

	private Long salesPoint;

	private Long reviewCount; // 리뷰 수

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "category_id", referencedColumnName = "id") // 외래키
	private Category categoryId; // 카테고리

	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	private List<Review> review;

	public void createReview(Double rating) {
		this.reviewCount++;
		this.rating += rating;
	}

	public void deleteReview(Double rating) {
		this.reviewCount--;
		this.rating -= rating;
	}
}
