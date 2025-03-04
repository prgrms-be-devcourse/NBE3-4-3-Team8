package com.ll.nbe342team8.domain.book.book.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.repository.BookRepository;
import com.ll.nbe342team8.domain.book.book.type.BookSortType;
import com.ll.nbe342team8.domain.book.book.type.SearchType;
import com.ll.nbe342team8.global.exceptions.ServiceException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private final ExternalBookApiService externalBookApiService;
	private final BookRepository bookRepository;

	public Page<Book> getAllBooks(int page, int pageSize, BookSortType bookSortType) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(bookSortType.getOrder());

		if (!(bookSortType == BookSortType.PUBLISHED_DATE)) { // 출간일을 보조 정렬 기준으로 추가
			BookSortType baseSort = BookSortType.PUBLISHED_DATE;
			sorts.add(baseSort.getOrder());
		}

		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
		return bookRepository.findAll(pageable);
	}

	public Book getBookById(Long id) {
		// Todo: GlobalExceptionHandler 를 통해 처리하도록 수정

		return bookRepository.findById(id)
				.orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "id에 해당하는 책이 없습니다."));
	}

	public long count() {
		return bookRepository.count();
	}

	public Book create(Book book) {
		return bookRepository.save(book);
	}

	public Book createReview(Book book, Double rating) {
		book.createReview(rating);
		return bookRepository.save(book);
	}

	public Book deleteReview(Book book, Double rating) {
		book.deleteReview(rating);
		return bookRepository.save(book);
	}

	@Transactional(readOnly = true)
	public Page<Book> searchBooks(int page, int pageSize, BookSortType bookSortType, SearchType searchType, String keyword) {
		Pageable pageable;
		// 판매량, 평점, 리뷰 정렬 시 보조 정렬 기준으로 출간일(pubDate) 적용
		if (bookSortType == BookSortType.SALES_POINT || bookSortType == BookSortType.RATING || bookSortType == BookSortType.REVIEW_COUNT) {
			pageable = PageRequest.of(page, pageSize, Sort.by(
					new Sort.Order(bookSortType.getOrder().getDirection(), bookSortType.getOrder().getProperty()),
					new Sort.Order(Sort.Direction.DESC, "pubDate")
			));
		} else {
			pageable = PageRequest.of(page, pageSize, Sort.by(bookSortType.getOrder()));
		}

		switch (searchType) {
			case AUTHOR:
				return bookRepository.findBooksByAuthorContaining(keyword, pageable);
			case ISBN13:
				return bookRepository.findBooksByIsbn13(keyword, pageable);
			case PUBLISHER:
				return bookRepository.findBooksByPublisherContaining(keyword, pageable);
			case TITLE:
			default:
				return bookRepository.findBooksByTitleContaining(keyword, pageable);
		}
	}
}

