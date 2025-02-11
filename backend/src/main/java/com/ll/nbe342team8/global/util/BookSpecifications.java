package com.ll.nbe342team8.global.util;

import org.springframework.data.jpa.domain.Specification;

import com.ll.nbe342team8.domain.book.book.entity.Book;

public class BookSpecifications {

	private BookSpecifications() {
		throw new UnsupportedOperationException("이 클래스는 유틸리티 클래스이며 인스턴스를 생성할 수 없습니다.");
	}

	public static Specification<Book> hasTitle(String title) {
		return (root, query, criteriaBuilder) -> {
			if (title == null || title.isEmpty()) {
				return null;
			}
			return criteriaBuilder.like(root.get("title"), "%" + title + "%");
		};
	}

	public static Specification<Book> hasAuthor(String author) {
		return (root, query, criteriaBuilder) -> {
			if (author == null || author.isEmpty()) {
				return null;
			}
			return criteriaBuilder.like(root.get("author"), "%" + author + "%");
		};
	}

	public static Specification<Book> hasIsbn(String isbn13) {
		return (root, query, criteriaBuilder) -> {
			if (isbn13 == null || isbn13.isEmpty()) {
				return null;
			}
			return criteriaBuilder.equal(root.get("isbn13"), isbn13);
		};
	}
}
