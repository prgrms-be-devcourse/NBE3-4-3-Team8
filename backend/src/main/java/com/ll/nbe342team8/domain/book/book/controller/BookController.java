package com.ll.nbe342team8.domain.book.book.controller;

import com.ll.nbe342team8.domain.book.book.dto.BookResponseDto;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.book.type.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookResponseDto> getAllBooks(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(defaultValue = "RECENT") SortType sortType) {

        List<Book> books = bookService.getAllBooks();
        return books.stream()
                .map(BookResponseDto::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{book-id}")
    public BookResponseDto getBookById(@PathVariable("book-id") long bookId) {
        Book book = bookService.getBookById(bookId);
        return BookResponseDto.from(book);
    }

    @GetMapping("/{book-id}/review")
    public void getBookReview(@PathVariable("book-id") long bookId) {

    }

    @GetMapping("/admin")
    public void getBooksAdmin() {

    }
}
