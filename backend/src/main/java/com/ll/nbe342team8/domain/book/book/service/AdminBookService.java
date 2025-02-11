package com.ll.nbe342team8.domain.book.book.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.nbe342team8.domain.book.book.dto.BookResponseDto;
import com.ll.nbe342team8.domain.book.book.dto.ExternalBookDto;
import com.ll.nbe342team8.domain.book.book.dto.request.AdminBookSearchDto;
import com.ll.nbe342team8.domain.book.book.dto.request.AdminBookUpdateDto;
import com.ll.nbe342team8.domain.book.book.dto.response.AdminBookDetailDto;
import com.ll.nbe342team8.domain.book.book.dto.response.AdminBookSearchListDto;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.repository.BookRepository;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBookService {

	private final ExternalBookApiService externalBookApiService;
	private final CategoryRepository categoryRepository;
	private final BookRepository bookRepository;

	// 도서 상품 검색
	public List<AdminBookSearchListDto> searchBooks(AdminBookSearchDto request) {
		return bookRepository.dynamicSearch(
						request.title(),
						request.author(),
						request.isbn13()
				).stream()
				.map(AdminBookSearchListDto::from)
				.toList();
	}


	// 도서 상품 등록
	@Transactional
	public boolean registerBook(String isbn13) {
		// 기존 DB에서 중복 확인
		if (bookRepository.existsByIsbn13(isbn13)) {
			throw new IllegalArgumentException("ISBN " + isbn13 + "은(는) 이미 등록된 도서입니다.");
		}

		// 외부 API에서 도서 정보 조회
		ExternalBookDto apiResponse = externalBookApiService.searchBookByIsbn13(isbn13);
		if (apiResponse == null) {
			throw new IllegalStateException("외부 API에서 ISBN " + isbn13 + "에 대한 도서를 찾을 수 없습니다.");
		}

		// 외부 API에서 받은 categoryNum을 category 테이블의 id와 매칭
		Category category = categoryRepository.findByCategoryId(apiResponse.categoryId())
				.orElseThrow(() -> new RuntimeException("해당 category_id가 존재하지 않습니다: " + apiResponse.categoryId()));

		// 변환
		BookResponseDto bookResponse = BookResponseDto.from(apiResponse);

		// DB 저장
		Book book = bookResponse.toEntity(category);
		bookRepository.save(book);

		return true;
	}

	// 도서 상품 상세 조회
	@Transactional(readOnly = true)
	public AdminBookDetailDto getBookDetail(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new IllegalArgumentException("해당 도서를 찾을 수 없습니다. ID: " + bookId));

		return AdminBookDetailDto.from(book);
	}

	// 도서 상품 수정
	@Transactional
	public AdminBookDetailDto updateBookPart(Long bookId, AdminBookUpdateDto request) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new IllegalArgumentException("해당 도서를 찾을 수 없습니다. ID: " + bookId));

		Book updatedBook = book.toBuilder()
				.title(request.title() != null ? request.title() : book.getTitle())
				.author(request.author() != null ? request.author() : book.getAuthor())
				.isbn(request.isbn() != null ? request.isbn() : book.getIsbn())
				.isbn13(request.isbn13() != null ? request.isbn13() : book.getIsbn13())
				.publisher(request.publisher() != null ? request.publisher() : book.getPublisher())
				.pubDate(request.pubDate() != null ? request.pubDate() : book.getPubDate())
				.priceStandard(request.priceStandard() != null ? request.priceStandard() : book.getPriceStandard())
				.pricesSales(request.priceSales() != null ? request.priceSales() : book.getPricesSales())
				.salesPoint(request.salesPoint() != null ? request.salesPoint() : book.getSalesPoint())
				.stock(request.stock() != null ? request.stock() : book.getStock())
				.status(request.status() != null ? request.status() : book.getStatus())
				.toc(request.toc() != null ? request.toc() : book.getToc())
				.coverImage(request.coverImage() != null ? request.coverImage() : book.getCoverImage())
				.description(request.description() != null ? request.description() : book.getDescription())
				.descriptionImage(request.descriptionImage() != null ? request.descriptionImage() : book.getDescriptionImage())
				.build();

		// 카테고리 변경이 필요한 경우만 업데이트
		if (request.categoryId() != null) {
			updatedBook = updatedBook.toBuilder()
					.categoryId(categoryRepository.findById(request.categoryId().longValue())
							.orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다.")))
					.build();
		}

		bookRepository.save(updatedBook);

		return AdminBookDetailDto.from(updatedBook);
	}

	// 도서 상품 삭제
	@Transactional
	public void deleteBook(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new IllegalArgumentException("해당 도서를 찾을 수 없습니다. ID: " + bookId));

		bookRepository.delete(book);
	}
}
