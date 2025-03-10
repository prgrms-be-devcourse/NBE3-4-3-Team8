package com.ll.nbe342team8.domain.book.book.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ll.nbe342team8.domain.book.category.entity.Category
import com.ll.nbe342team8.domain.book.review.entity.Review
import com.ll.nbe342team8.global.jpa.entity.BaseTime
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.Formula
import java.time.LocalDate

@Entity
open class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null,

    @Column(length = 100)
    @NotNull
    open var title: String = "",

    @NotNull
    open var author: String = "",

    open var publisher: String? = null,

    open var isbn: String? = null,

    @NotNull
    open var isbn13: String = "",

    @NotNull
    open var pubDate: LocalDate = LocalDate.now(),

    @NotNull
    open var priceStandard: Int = 0,

    @NotNull
    open var pricesSales: Int = 0,

    @NotNull
    open var stock: Int = 0,

    @NotNull
    open var status: Int = 0,

    open var rating: Double = 0.0,

    @Formula("CASE WHEN review_count = 0 THEN 0 ELSE rating / review_count END")
    open var averageRating: Double? = null,

    @Column(columnDefinition = "TEXT")
    open var toc: String? = null,

    open var coverImage: String = "",

    open var description: String? = null,

    open var descriptionImage: String? = null,

    open var salesPoint: Long? = null,

    open var reviewCount: Long = 0,

    @JsonIgnore
    @ManyToOne
    @NotNull
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    open var categoryId: Category,

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    open var review: MutableList<Review> = mutableListOf()
) : BaseTime() {

    open fun createReview(rating: Double) {
        this.reviewCount++
        this.rating += rating
    }

    open fun deleteReview(rating: Double) {
        this.reviewCount--
        this.rating -= rating
    }

    // 빌더 패턴을 위한 Builder 클래스
    class Builder {
        private var id: Long? = null
        private var title: String = ""
        private var author: String = ""
        private var publisher: String? = null
        private var isbn: String? = null
        private var isbn13: String = ""
        private var pubDate: LocalDate = LocalDate.now()
        private var priceStandard: Int = 0
        private var pricesSales: Int = 0
        private var stock: Int = 0
        private var status: Int = 0
        private var rating: Double = 0.0
        private var averageRating: Double? = null
        private var toc: String? = null
        private var coverImage: String = ""
        private var description: String? = null
        private var descriptionImage: String? = null
        private var salesPoint: Long? = null
        private var reviewCount: Long = 0
        private var categoryId: Category = Category()
        private var review: MutableList<Review> = mutableListOf()

        fun id(id: Long?) = apply { this.id = id }
        fun title(title: String) = apply { this.title = title }
        fun author(author: String) = apply { this.author = author }
        fun publisher(publisher: String?) = apply { this.publisher = publisher }
        fun isbn(isbn: String?) = apply { this.isbn = isbn }
        fun isbn13(isbn13: String) = apply { this.isbn13 = isbn13 }
        fun pubDate(pubDate: LocalDate) = apply { this.pubDate = pubDate }
        fun priceStandard(priceStandard: Int) = apply { this.priceStandard = priceStandard }
        fun pricesSales(pricesSales: Int) = apply { this.pricesSales = pricesSales }
        fun stock(stock: Int) = apply { this.stock = stock }
        fun status(status: Int) = apply { this.status = status }
        fun rating(rating: Double) = apply { this.rating = rating }
        fun averageRating(averageRating: Double?) = apply { this.averageRating = averageRating }
        fun toc(toc: String?) = apply { this.toc = toc }
        fun coverImage(coverImage: String) = apply { this.coverImage = coverImage }
        fun description(description: String?) = apply { this.description = description }
        fun descriptionImage(descriptionImage: String?) = apply { this.descriptionImage = descriptionImage }
        fun salesPoint(salesPoint: Long?) = apply { this.salesPoint = salesPoint }
        fun reviewCount(reviewCount: Long) = apply { this.reviewCount = reviewCount }
        fun categoryId(categoryId: Category) = apply { this.categoryId = categoryId }
        fun review(review: MutableList<Review>) = apply { this.review = review }

        fun build(): Book {
            return Book(
                id = id,
                title = title,
                author = author,
                publisher = publisher,
                isbn = isbn,
                isbn13 = isbn13,
                pubDate = pubDate,
                priceStandard = priceStandard,
                pricesSales = pricesSales,
                stock = stock,
                status = status,
                rating = rating,
                averageRating = averageRating,
                toc = toc,
                coverImage = coverImage,
                description = description,
                descriptionImage = descriptionImage,
                salesPoint = salesPoint,
                reviewCount = reviewCount,
                categoryId = categoryId,
                review = review
            )
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}
