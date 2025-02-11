package com.ll.nbe342team8.domain.book.book.dto.response;

import java.time.LocalDate;

import com.ll.nbe342team8.domain.book.book.entity.Book;

// 관리자 페이지에서 도서 목록을 조회할 때 사용하는 DTO
public record AdminBookListDto(
		Long id,             // 책 ID
		String title,        // 제목
		String author,       // 저자
		String publisher,    // 출판사
		LocalDate pubDate,   // 출판일
		String categoryName, // 카테고리 이름
		String coverImage,   // 커버 이미지 URL
		int priceStandard,   // 정가
		int pricesSales,     // 판매가
		int stock,           // 재고
		int status           // 판매 상태
) {
	public static AdminBookListDto from(Book book) {
		return new AdminBookListDto(
				book.getId(),
				book.getTitle(),
				book.getAuthor(),
				book.getPublisher(),
				book.getPubDate(),
				book.getCategoryId().getCategoryName(),
				book.getCoverImage(),
				book.getPriceStandard(),
				book.getPricesSales(),
				book.getStock(),
				book.getStatus()
		);
	}
}
