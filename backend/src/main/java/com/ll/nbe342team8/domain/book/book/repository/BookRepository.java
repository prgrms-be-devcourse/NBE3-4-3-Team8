package com.ll.nbe342team8.domain.book.book.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.global.util.BookSpecifications;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
	List<Book> findAll();

	Page<Book> findAll(Pageable pageable);

	// 도서 등록시 중복 확인
	boolean existsByIsbn13(String isbn13);

	// 도서 등록을 위한 도서 검색
	default List<Book> dynamicSearch(String title, String author, String isbn13) {
		Specification<Book> spec = Specification.where(null); // 기본값

		if (title != null && !title.isEmpty()) {
			spec = spec.and(BookSpecifications.hasTitle(title));
		}
		if (author != null && !author.isEmpty()) {
			spec = spec.and(BookSpecifications.hasAuthor(author));
		}
		if (isbn13 != null && !isbn13.isEmpty()) {
			spec = spec.and(BookSpecifications.hasIsbn(isbn13));
		}

		return findAll(spec);
	}

	Page<Book> findBooksByTitleContaining(String title, Pageable pageable);

	Page<Book> findBooksByAuthorContaining(String author, Pageable pageable);

	Page<Book> findBooksByIsbn13(String isbn13, Pageable pageable);

	Page<Book> findBooksByPublisherContaining(String publisher, Pageable pageable);
}
