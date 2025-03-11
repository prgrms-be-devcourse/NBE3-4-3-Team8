package com.ll.nbe342team8.domain.book.book.controller

import com.ll.nbe342team8.domain.book.book.dto.BookResponseDto
import com.ll.nbe342team8.domain.book.book.entity.Book
import com.ll.nbe342team8.domain.book.book.service.BookService
import com.ll.nbe342team8.domain.book.book.type.BookSortType
import com.ll.nbe342team8.domain.book.book.type.SearchType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.hibernate.validator.constraints.Range
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import java.util.function.Function

@Slf4j
@RestController
@RequestMapping("/books")
@Tag(name = "Book", description = "Book API")
@RequiredArgsConstructor
class BookController (
    private val bookService: BookService
){

    @GetMapping
    @Operation(summary = "전체 도서 조회")
    fun getAllBooks(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") pageSize: @Range(min = 0, max = 100) Int,
        @RequestParam(defaultValue = "PUBLISHED_DATE") bookSortType: BookSortType
    ): Page<BookResponseDto> {
        val books = bookService.getAllBooks(page, pageSize, bookSortType)
        return books.map<BookResponseDto>(Function { book: Book -> BookResponseDto.from(book) })
    }

    @Operation(summary = "특정 도서 조회")
    @GetMapping("/{bookId}")
    fun getBookById(@PathVariable bookId: Long): BookResponseDto {
        val book = bookService.getBookById(bookId)
        return BookResponseDto.from(book)
    }

    @Operation(summary = "도서 검색 (제목, 저자, ISBN13, 출판사 검색)")
    @GetMapping("/search")
    fun searchBooks(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") pageSize: @Range(min = 0, max = 100) Int,
        @RequestParam(defaultValue = "PUBLISHED_DATE") bookSortType: BookSortType,
        @RequestParam(defaultValue = "TITLE") searchType: SearchType,
        @RequestParam keyword: String
    ): Page<BookResponseDto> {
        val books = bookService.searchBooks(page, pageSize, bookSortType, searchType, keyword)
        return books.map<BookResponseDto?>(Function { book: Book -> BookResponseDto.from(book) })
    }
}
