package com.ll.nbe342team8.domain.book.book.controller;

import java.net.URI;
import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.nbe342team8.domain.book.book.dto.request.AdminBookRegisterDto;
import com.ll.nbe342team8.domain.book.book.dto.request.AdminBookSearchDto;
import com.ll.nbe342team8.domain.book.book.dto.request.AdminBookUpdateDto;
import com.ll.nbe342team8.domain.book.book.dto.response.AdminBookDetailDto;
import com.ll.nbe342team8.domain.book.book.dto.response.AdminBookListDto;
import com.ll.nbe342team8.domain.book.book.dto.response.AdminBookSearchListDto;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.AdminBookService;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.book.type.BookSortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/books")
@Tag(name = "관리자 - 상품(도서) 관리", description = "관리자 상품 관리 API")
@RequiredArgsConstructor
public class AdminBookController {

	private final BookService bookService;
	private final AdminBookService adminBookService;

	@GetMapping
	@Operation(summary = "전체 도서 조회", description = "DB 전체 도서를 조회한다.(페이징)")
	public Page<AdminBookListDto> getAllBooks(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") @Range(min = 0, max = 100) int pageSize,
			@RequestParam(defaultValue = "PUBLISHED_DATE") BookSortType bookSortType) {

		Page<Book> books = bookService.getAllBooks(page, pageSize, bookSortType);
		return books.map(AdminBookListDto::from);
	}

	// 외부 API에서 도서 정보를 가져와서 등록하는 기능(개선 예정)
	@PostMapping("/search")
	@Operation(summary = "도서 검색")
	public ResponseEntity<List<AdminBookSearchListDto>> searchBooks(@RequestBody AdminBookSearchDto request) {
		List<AdminBookSearchListDto> responses = adminBookService.searchBooks(request);
		return ResponseEntity.ok(responses);
	}

	@PostMapping("/register")
	@Operation(summary = "도서 등록")
	public ResponseEntity<String> registerBook(@RequestBody AdminBookRegisterDto request) {
		boolean isRegistered = adminBookService.registerBook(request.isbn13());

		if (isRegistered) {
			URI location = URI.create("/books/" + request.isbn13());
			return ResponseEntity.created(location).body("도서가 성공적으로 등록되었습니다.");
		} else {
			return ResponseEntity.badRequest().body("등록 실패: 해당 도서를 찾을 수 없습니다.");
		}
	}

	@GetMapping("/{bookId}")
	@Operation(summary = "도서 상세 조회", description = "상품(도서)의 상세 정보를 조회한다.")
	public ResponseEntity<AdminBookDetailDto> getBookDetail(@PathVariable Long bookId) {
		AdminBookDetailDto bookDetail = adminBookService.getBookDetail(bookId);

		return ResponseEntity.ok(bookDetail);
	}

	@PatchMapping("/{bookId}")
	@Operation(summary = "도서 수정", description = "특정 도서 정보를 수정한다.")
	public ResponseEntity<AdminBookDetailDto> updateBookPart(
			@PathVariable Long bookId,
			@RequestBody AdminBookUpdateDto requestDto) {

		AdminBookDetailDto updatedBook = adminBookService.updateBookPart(bookId, requestDto);

		return ResponseEntity.ok(updatedBook);
	}

	@DeleteMapping("/{bookId}")
	@Operation(summary = "도서 삭제", description = "특정 도서를 삭제한다.")
	public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
		adminBookService.deleteBook(bookId);

		return ResponseEntity.ok("도서가 성공적으로 삭제되었습니다.");
	}
}
