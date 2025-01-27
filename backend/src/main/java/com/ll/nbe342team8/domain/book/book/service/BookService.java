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
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
    }

    public long count() {
        return bookRepository.count();
    }

    public void create(Book book) {
        bookRepository.save(book);
    }
}
