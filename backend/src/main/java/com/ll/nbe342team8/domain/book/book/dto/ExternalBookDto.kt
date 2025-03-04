package com.ll.nbe342team8.domain.book.book.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// 외부 API에서 받아온 도서 정보를 담는 DTO
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalBookDto(
		String title,             // 제목
		String author,            // 저자
		String publisher,         // 출판사
		LocalDate pubDate,        // 출판일
		String isbn,              // ISBN
		String isbn13,            // ISBN13
		Integer priceStandard,    // 정가
		Integer priceSales,       // 판매가
		String toc,               // 목차
		String cover,             // 커버 이미지 URL
		String description,       // 상세 설명
		String descriptionImage,  // 상세 설명 이미지 URL
		Integer categoryId        // 알라딘 API에서 받아온 카테고리 ID (Category Pk 아님)
) {
}
