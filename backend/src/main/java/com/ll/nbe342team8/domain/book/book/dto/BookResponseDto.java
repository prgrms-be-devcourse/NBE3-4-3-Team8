package com.ll.nbe342team8.domain.book.book.dto;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String isbn13;
    private LocalDate pubDate;
    private int price;
    private int stock;
    private int status;
    private float rating;
    private String toc;
    private String cover;
    private String description;
    private String descriptionImage;
    private Category categoryId;

    public static BookResponseDto fromEntity(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .isbn13(book.getIsbn13())
                .pubDate(book.getPubDate())
                .price(book.getPrice())
                .stock(book.getStock())
                .status(book.getStatus())
                .rating(book.getRating())
                .toc(book.getToc())
                .cover(book.getCover())
                .description(book.getDescription())
                .descriptionImage(book.getDescriptionImage())
                .categoryId(book.getCategoryId())
                .build();
    }
}
