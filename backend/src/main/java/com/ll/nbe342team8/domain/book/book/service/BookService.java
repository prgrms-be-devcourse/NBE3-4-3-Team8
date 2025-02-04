package com.ll.nbe342team8.domain.book.book.service;

import com.ll.nbe342team8.domain.book.book.dto.ExternalBookDto;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookService {

    private final ExternalBookApiService externalBookApiService;
    private final BookRepository bookRepository;

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
                .price(dto.getPrice())
                .toc(dto.getToc())
                .cover(dto.getCover())
                .description(dto.getDescription())
                .descriptionImage(dto.getDescriptionImage())
                .categoryId(dto.getCategoryId())
                .build();
    }

}
