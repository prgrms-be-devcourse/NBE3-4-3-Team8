package com.ll.nbe342team8.domain.book.book.controller;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.book.type.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    public List<Book> getBooks(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(defaultValue = "RECENT") SortType sortType) {

        List<Book> books = bookService.getAllBooks();
        return books;
    }

    @GetMapping("/{book-id}")
    public void getBookById(@PathVariable("book-id") int bookId) {

    }

    @GetMapping("/{book-id}/review")
    public void getBookReview(@PathVariable("book-id") int bookId) {

    }

    @GetMapping("/admin")
    public void getBooksAdmin() {

    }
}
