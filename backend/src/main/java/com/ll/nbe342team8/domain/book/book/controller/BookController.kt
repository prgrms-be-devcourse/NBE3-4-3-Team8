package com.ll.nbe342team8.domain.book.book.controller;

import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.nbe342team8.domain.book.book.dto.BookResponseDto;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.book.type.BookSortType;
import com.ll.nbe342team8.domain.book.book.type.SearchType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/books")
@Tag(name = "Book", description = "Book API")
@RequiredArgsConstructor
public class BookController {
	private final BookService bookService;

	@GetMapping
	@Operation(summary = "전체 도서 조회")
	public Page<BookResponseDto> getAllBooks(@RequestParam(defaultValue = "0") int page,
											 @RequestParam(defaultValue = "10") @Range(min = 0, max = 100) int pageSize,
											 @RequestParam(defaultValue = "PUBLISHED_DATE") BookSortType bookSortType) {

		Page<Book> books = bookService.getAllBooks(page, pageSize, bookSortType);
		return books.map(BookResponseDto::from);
	}

	@Operation(summary = "특정 도서 조회")
	@GetMapping("/{bookId}")
	public BookResponseDto getBookById(@PathVariable Long bookId) {
		Book book = bookService.getBookById(bookId);
		return BookResponseDto.from(book);
	}

	@Operation(summary = "도서 검색 (제목, 저자, ISBN13, 출판사 검색)")
	@GetMapping("/search")
	public Page<BookResponseDto> searchBooks(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") @Range(min = 0, max = 100) int pageSize,
			@RequestParam(defaultValue = "PUBLISHED_DATE") BookSortType bookSortType,
			@RequestParam(defaultValue = "TITLE") SearchType searchType,
			@RequestParam String keyword) {

		Page<Book> books = bookService.searchBooks(page, pageSize, bookSortType, searchType, keyword);
		return books.map(BookResponseDto::from);
	}
}
