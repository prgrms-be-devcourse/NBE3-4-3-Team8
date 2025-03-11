package com.ll.nbe342team8.domain.book.book.dto

import com.ll.nbe342team8.domain.book.book.entity.Book
import com.ll.nbe342team8.domain.book.category.entity.Category
import java.time.LocalDate

data class BookResponseDto(
    val id: Long?,
    val title: String,
    val author: String,
    val isbn: String?,
    val isbn13: String,
    val publisher: String?,
    val pubDate: LocalDate,
    val priceStandard: Int,
    val priceSales: Int,
    val salesPoint: Long,
    val stock: Int,
    val status: Int,
    val rating: Double,
    val toc: String,
    val reviewCount: Long,
    val coverImage: String,
    val categoryId: Int,
    val description: String,
    val descriptionImage: String,
    val averageRating: Double
) {
    companion object {
        fun from(book: Book): BookResponseDto {
            return BookResponseDto(
                id = book.id,
                title = book.title,
                author = book.author,
                isbn = book.isbn,
                isbn13 = book.isbn13,
                publisher = book.publisher,
                pubDate = book.pubDate,
                priceStandard = book.priceStandard,
                priceSales = book.pricesSales,
                salesPoint = book.salesPoint ?: 0L,
                stock = book.stock,
                status = book.status,
                rating = book.rating,
                toc = book.toc ?: "",
                reviewCount = book.reviewCount,
                coverImage = book.coverImage,
                categoryId = book.categoryId.categoryId,
                description = book.description ?: "",
                descriptionImage = book.descriptionImage ?: "",
                averageRating = book.averageRating ?: 0.0
            )
        }

        fun from(externalBookDto: ExternalBookDto): BookResponseDto {
            return BookResponseDto(
                id = null, // DB 저장 시 자동 생성
                title = externalBookDto.title ?: "",
                author = externalBookDto.author ?: "",
                isbn = externalBookDto.isbn ?: "0000000000000",
                isbn13 = externalBookDto.isbn13 ?: "9999999999999",
                publisher = externalBookDto.publisher ?: "Unknown",
                pubDate = externalBookDto.pubDate ?: LocalDate.of(9999, 12, 31),
                priceStandard = externalBookDto.priceStandard ?: 9999999,
                priceSales = externalBookDto.priceSales ?: 9999999,
                salesPoint = 0L,
                stock = 0,
                status = 0,
                rating = 0.0,
                toc = externalBookDto.toc ?: "",
                reviewCount = 0L,
                coverImage = externalBookDto.cover ?: "",
                categoryId = externalBookDto.categoryId ?: 99999,
                description = externalBookDto.description ?: "",
                descriptionImage = externalBookDto.descriptionImage ?: "",
                averageRating = 0.0
            )
        }
    }

    // BookResponseDto -> Book 엔티티 변환 (Category는 외부에서 제공)
    fun toEntity(category: Category): Book {
        return Book.builder()
            .title(if (this.title.isNotEmpty()) this.title else "")
            .author(if (this.author.isNotEmpty()) this.author else "")
            .publisher(this.publisher)
            .isbn(this.isbn)
            .isbn13(if (this.isbn13.isNotEmpty()) this.isbn13 else "9999999999999")
            .pubDate(this.pubDate)
            .priceStandard(this.priceStandard)
            .pricesSales(this.priceSales)
            .stock(this.stock)
            .status(this.status)
            .salesPoint(this.salesPoint)
            .reviewCount(this.reviewCount)
            .rating(this.rating)
            .averageRating(this.averageRating)
            .toc(this.toc.ifEmpty { "" })
            .coverImage(this.coverImage.ifEmpty { "" })
            .description(this.description.ifEmpty { "" })
            .descriptionImage(this.descriptionImage.ifEmpty { "" })
            .categoryId(category)
            .build()
    }
}