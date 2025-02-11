package com.ll.nbe342team8.domain.book.book.dto.response;

import java.time.LocalDate;

import com.ll.nbe342team8.domain.book.book.entity.Book;

// 관리자 페이지에서 도서 상세 정보를 조회할 때 사용하는 DTO
public record AdminBookDetailDto(
		Long id,
		String title,
		String author,
		String publisher,
		LocalDate pubDate,
		String category,
		String isbn,
		String isbn13,
		String coverImage,
		String toc,
		String description,
		String descriptionImage,
		int priceStandard,
		int pricesSales,
		int stock,
		int status,
		long salesPoint,
		Double rating,
		long reviewCount
) {
	public static AdminBookDetailDto from(Book book) {
		return new AdminBookDetailDto(
				book.getId(),
				book.getTitle(),
				book.getAuthor(),
				book.getPublisher(),
				book.getPubDate(),
				book.getCategoryId().getCategory(), // 전체 카테고리(대>중>소) 이름
				book.getIsbn(),
				book.getIsbn13(),
				book.getCoverImage(),
				book.getToc(),
				book.getDescription(),
				book.getDescriptionImage(),
				book.getPriceStandard(),
				book.getPricesSales(),
				book.getStock(),
				book.getStatus(),
				book.getSalesPoint(),
				book.getRating(),
				book.getReviewCount()
		);
	}
}
