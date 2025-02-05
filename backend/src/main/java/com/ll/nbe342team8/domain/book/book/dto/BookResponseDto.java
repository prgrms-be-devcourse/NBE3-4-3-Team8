package com.ll.nbe342team8.domain.book.book.dto;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import java.time.LocalDate;

public record BookResponseDto(
        Long id,
        String title,
        String author,
        String isbn,
        String isbn13,
        String publisher,
        LocalDate pubDate,
        int priceStandard,
        int priceSales,
        long salesPoint,
        int stock,
        int status,
        float rating,
        String toc,
        long reviewCount,
        String coverImage,
        Integer categoryId,
        String description,
        String descriptionImage,
        float averageRating  // 추가한 평균 평점 필드
) {
    public static BookResponseDto from(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getIsbn13(),
                book.getPublisher(),
                book.getPubDate(),
                book.getPriceStandard(),
                book.getPricesSales(),
                book.getSalesPoint(),
                book.getStock(),
                book.getStatus(),
                book.getRating(),
                book.getToc(),
                book.getReviewCount(),
                book.getCoverImage(),
                book.getCategoryId().getCategoryId(),
                book.getDescription(),
                book.getDescriptionImage(),
                book.getAverageRating()
        );
    }
}
