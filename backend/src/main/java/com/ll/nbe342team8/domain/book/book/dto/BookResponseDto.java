package com.ll.nbe342team8.domain.book.book.dto;

import com.ll.nbe342team8.domain.book.book.entity.Book;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookResponseDto(Long id,
                              String title,
                              String author,
                              String publisher,
                              LocalDate pubDate,
                              int priceStandard,
                              int priceSales,
                              long salesPoint,
                              int stock,
                              float rating,
                              long reviewCount,
                              String coverImage,
                              Integer categoryId) {

    public static BookResponseDto from(Book book){
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPubDate(),
                book.getPriceStandard(),
                book.getPricesSales(),
                book.getSalesPoint(),
                book.getStock(),
                book.getRating(),
                book.getReviewCount(),
                book.getCoverImage(),
                book.getCategoryId().getCategoryId()
        );
    }
}
