package com.ll.nbe342team8.domain.book.book.dto.request;

// 관리자 페이지에서 등록할 도서 검색할 때 사용하는 DTO
public record AdminBookSearchDto(
		String title,
		String author,
		String isbn13
) {
}
