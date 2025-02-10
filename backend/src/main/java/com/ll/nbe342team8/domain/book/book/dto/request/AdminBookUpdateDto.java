package com.ll.nbe342team8.domain.book.book.dto.request;

import java.time.LocalDate;

// 관리자 페이지에서 도서 정보 수정할 때 사용하는 DTO
public record AdminBookUpdateDto(
		String title,
		String author,
		String isbn,
		String isbn13,
		String publisher,
		LocalDate pubDate,
		Integer priceStandard,
		Integer priceSales,
		Long salesPoint,
		Integer stock,
		Integer status,
		String toc,
		String coverImage,
		Integer categoryId,
		String description,
		String descriptionImage
) {
}
