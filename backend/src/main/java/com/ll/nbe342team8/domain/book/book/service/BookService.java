package com.ll.nbe342team8.domain.book.book.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.repository.BookRepository;
import com.ll.nbe342team8.domain.book.book.type.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

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

    public Page<Book> searchBooks(int page, int pageSize, SortType sortType, String title){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(sortType.getOrder());

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        return bookRepository.findBooksByTitleContaining(title, pageable);
    }
}