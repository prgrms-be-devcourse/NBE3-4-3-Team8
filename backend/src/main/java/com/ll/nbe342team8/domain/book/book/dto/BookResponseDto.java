package com.ll.nbe342team8.domain.book.book.dto;

import com.ll.nbe342team8.domain.book.book.entity.Book;

public record BookResponseDto(Long id,
                              String title,
                              String author,
                              int price,
                              int stock,
                              float rating,
                              String coverImage,
                              Integer categoryId,
                              long salesPoint) {

    public static BookResponseDto from(Book book){
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getStock(),
                book.getRating(),
                book.getCoverImage(),
                book.getCategoryId().getCategoryId(),
                book.getSalesPoint()
        );
    }
}
