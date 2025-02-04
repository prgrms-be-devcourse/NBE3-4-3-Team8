package com.ll.nbe342team8.domain.book.book.service;

import com.ll.nbe342team8.domain.book.book.dto.BookPatchRequestDto;
import com.ll.nbe342team8.domain.book.book.dto.BookResponseDto;
import com.ll.nbe342team8.domain.book.book.dto.ExternalBookDto;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.repository.BookRepository;
import com.ll.nbe342team8.domain.book.book.type.SortType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final ExternalBookApiService externalBookApiService;
    private final BookRepository bookRepository;

    public Page<Book> getAllBooks(int page, int pageSize, SortType sortType) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(sortType.getOrder());

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        return bookRepository.findAll(pageable);
    }

    public Book getBookById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID 값이 null입니다.");
        }
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID(" + id + ")의 책을 찾을 수 없습니다."));
    }

    public long count() {
        return bookRepository.count();
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public Book createReview(Book book, float rating) {
        book.createReview(rating);
        return bookRepository.save(book);
    }

    public Book deleteReview(Book book, float rating) {
        book.deleteReview(rating);
        return bookRepository.save(book);
    }

    public Page<Book> searchBooks(int page, int pageSize, SortType sortType, String title) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(sortType.getOrder());

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        return bookRepository.findBooksByTitleContaining(title, pageable);
    }

    // 도서 추가
    @Transactional
    public Book addBook(String isbn13) {

        if (bookRepository.existsByIsbn13(isbn13)) {
            throw new IllegalArgumentException("이미 등록된 도서입니다.");
        }

        ExternalBookDto externalBookDto = externalBookApiService.searchBook(isbn13);

        Book book = mapToEntity(externalBookDto);

        return bookRepository.save(book);
    }

    private Book mapToEntity(ExternalBookDto dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .isbn13(dto.getIsbn13())
                .pubDate(LocalDate.parse(dto.getPubDate()))
                .priceStandard(dto.getPriceStandard())
                .pricesSales(dto.getPriceSales())
                .toc(dto.getToc())
                .coverImage(dto.getCover())
                .description(dto.getDescription())
                .descriptionImage(dto.getDescriptionImage())
                .categoryId(dto.getCategoryId())
                .build();
    }

    // 도서 정보 수정
    @Transactional
    public BookResponseDto updateBookPart(Long bookId, BookPatchRequestDto requestDto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("책을 찾을 수 없습니다."));

        book.update(requestDto); // DTO 에서 null 이 아닌 값만 업데이트

        return BookResponseDto.from(book);
    }
}