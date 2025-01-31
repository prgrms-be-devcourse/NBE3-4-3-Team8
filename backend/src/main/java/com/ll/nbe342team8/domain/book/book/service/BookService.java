package com.ll.nbe342team8.domain.book.book.service;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    public List<Book> getAllBooks(){
        List<Book> books = bookRepository.findAll();
        return books;
    }

    public Book getBookById(Long id){
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
}
