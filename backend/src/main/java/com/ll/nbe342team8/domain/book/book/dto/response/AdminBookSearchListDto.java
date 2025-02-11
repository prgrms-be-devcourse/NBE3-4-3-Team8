package com.ll.nbe342team8.domain.book.book.dto.response;

import com.ll.nbe342team8.domain.book.book.entity.Book;

// 관리자 페이지에서 도서 등록을 위해 알라딘API 도서 검색 시 사용하는 DTO
public record AdminBookSearchListDto(
		String title,
		String author,
		String publisher,
		String pubDate,
		String categoryName,
		String isbn13
) {
	public static AdminBookSearchListDto from(Book book) {
		return new AdminBookSearchListDto(
				book.getTitle(),
				book.getAuthor(),
				book.getPublisher(),
				book.getPubDate().toString(),
				book.getCategoryId().getCategoryName(),
				book.getIsbn13()
		);
	}
}
