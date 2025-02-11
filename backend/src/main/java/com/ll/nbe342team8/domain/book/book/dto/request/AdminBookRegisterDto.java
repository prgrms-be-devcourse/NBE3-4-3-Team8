package com.ll.nbe342team8.domain.book.book.dto.request;

// 관리자 페이지에서 도서 등록 요청할 때 사용하는 DTO
public record AdminBookRegisterDto(
		String isbn13
) {
}
