package com.ll.nbe342team8.domain.book.book.dto;

import java.time.LocalDate;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.category.entity.Category;

public record BookResponseDto(
		Long id,
		String title,
		String author,
		String isbn,
		String isbn13,
		String publisher,
		LocalDate pubDate,
		int priceStandard,
		int priceSales,
		Long salesPoint,
		int stock,
		int status,
		Double rating,
		String toc,
		Long reviewCount,
		String coverImage,
		Integer categoryId,
		String description,
		String descriptionImage,
		Double averageRating  // 추가한 평균 평점 필드
) {
	public static BookResponseDto from(Book book) {
		return new BookResponseDto(
				book.getId(),
				book.getTitle(),
				book.getAuthor(),
				book.getIsbn(),
				book.getIsbn13(),
				book.getPublisher(),
				book.getPubDate(),
				book.getPriceStandard(),
				book.getPricesSales(),
				book.getSalesPoint(),
				book.getStock(),
				book.getStatus(),
				book.getRating(),
				book.getToc(),
				book.getReviewCount(),
				book.getCoverImage(),
				book.getCategoryId().getCategoryId(),
				book.getDescription(),
				book.getDescriptionImage(),
				book.getAverageRating()
		);
	}

	// 외부 API ExternalBookDto -> 내부 DTO 변환
	// 여기서는 @NotNull인 필드에 대해 null 체크 후 기본값 적용
	public static BookResponseDto from(ExternalBookDto externalBookDto) {
		return new BookResponseDto(
				null, // id는 DB 저장 시 자동 생성
				externalBookDto.title() != null ? externalBookDto.title() : "", // 필수: 제목
				externalBookDto.author() != null ? externalBookDto.author() : "", // 필수: 저자
				externalBookDto.isbn() != null ? externalBookDto.isbn() : "0000000000000", // 선택: ISBN
				externalBookDto.isbn13() != null ? externalBookDto.isbn13() : "9999999999999", // 필수: ISBN13
				externalBookDto.publisher() != null ? externalBookDto.publisher() : "Unknown", // 선택: 출판사
				externalBookDto.pubDate() != null ? externalBookDto.pubDate() : LocalDate.of(9999, 12, 31), // 필수: 출판일
				externalBookDto.priceStandard() != null ? externalBookDto.priceStandard() : 9999999, // 필수: 정가
				externalBookDto.priceSales() != null ? externalBookDto.priceSales() : 9999999, // 필수: 판매가
				0L, // 필수: 판매 포인트 (기본값 0L)
				0, // 필수: 재고 (외부 데이터에 없으므로 0 기본)
				0, // 필수: 판매 상태 (외부 데이터에 없으므로 0 기본, 품절)
				0.0, // 선택: 평점 (없으면 0.0 기본값)
				externalBookDto.toc() != null ? externalBookDto.toc() : "", // 선택: 목차
				0L, // 선택: 리뷰 수 (없으면 0L 기본값)
				externalBookDto.cover() != null ? externalBookDto.cover() : "", // 선택: 커버 이미지
				externalBookDto.categoryId() != null ? externalBookDto.categoryId() : 99999, // 필수: 카테고리 ID (없으면 99999로 기본)
				externalBookDto.description() != null ? externalBookDto.description() : "", // 선택: 상세 설명
				externalBookDto.descriptionImage() != null ? externalBookDto.descriptionImage() : "", // 선택: 상세 설명 이미지
				0.0 // 선택: 평균 평점 (없으면 0.0 기본값)
		);
	}


	// BookResponseDto -> Book 엔티티 변환
	public Book toEntity(Category category) {
		return Book.builder()
				.title(this.title != null ? this.title : "")                                 // 필수: 제목
				.author(this.author != null ? this.author : "")                              // 필수: 저자
				.publisher(this.publisher)                                                   // 선택: 출판사
				.isbn(this.isbn)                                                             // 선택: ISBN
				.isbn13(this.isbn13 != null ? this.isbn13 : "9999999999999")                 // 필수: ISBN13
				.pubDate(this.pubDate != null ? this.pubDate : LocalDate.of(9999, 12, 31))   // 필수: 출판일
				.priceStandard(this.priceStandard)                                           // 필수: 정가
				.pricesSales(this.priceSales)                                               // 필수: 판매가
				.stock(this.stock)                                                           // 필수: 재고
				.status(this.status)                                                         // 필수: 상태
				.salesPoint(this.salesPoint != null ? this.salesPoint : 0L)                  // 선택: 판매량
				.reviewCount(this.reviewCount != null ? this.reviewCount : 0L)               // 선택: 리뷰 수
				.rating(this.rating != null ? this.rating : 0.0)                             // 선택: 평점
				.averageRating(this.averageRating != null ? this.averageRating : 0.0)        // 선택: 평균 평점
				.toc(this.toc != null ? this.toc : "")                                       // 선택: 목차
				.coverImage(this.coverImage != null ? this.coverImage : "")                  // 선택: 커버 이미지
				.description(this.description != null ? this.description : "")               // 선택: 상세 설명
				.descriptionImage(this.descriptionImage != null ? this.descriptionImage : "")// 선택: 상세 설명 이미지
				.categoryId(category)                                                          // 필수: 카테고리 (외부에서 제공해야 함)
				.build();
	}
}
