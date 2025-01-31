package com.ll.nbe342team8.domain.book.book.dto;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public record BookResponseDto(Long id,
                              String title,
                              String author,
                              int price,
                              int stock,
                              float rating,
                              String image,
                              String category) {

    public static BookResponseDto from(Book book){
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getStock(),
                book.getRating(),
                book.getImage(),
                book.getCategory().getCategory()
        );
    }
}
