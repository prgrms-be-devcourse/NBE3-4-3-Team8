package com.ll.nbe342team8.domain.book.review.entity

import com.ll.nbe342team8.domain.book.book.entity.Book
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.global.jpa.entity.BaseTime
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
class Review(

    @field:NotNull
    @ManyToOne(fetch = FetchType.EAGER) // EAGER로 변경
    var book: Book,

    @field:NotNull
    @ManyToOne(fetch = FetchType.EAGER) // EAGER로 변경
    var member: Member,

    @field:NotNull
    var content: String,

    @field:NotNull
    var rating: Double

) : BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set

    fun update(content: String, rating: Double) {
        this.content = content
        this.rating = rating
    }

    companion object {
        @JvmStatic
        fun create(book: Book, member: Member, content: String, rating: Double): Review {
            return Review(book, member, content, rating)
        }

        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var book: Book? = null
        private var member: Member? = null
        private var content: String? = null
        private var rating: Double? = null

        fun book(book: Book) = apply { this.book = book }
        fun member(member: Member) = apply { this.member = member }
        fun content(content: String) = apply { this.content = content }
        fun rating(rating: Double) = apply { this.rating = rating }

        fun build(): Review {
            val bookVal = book ?: throw IllegalArgumentException("book must not be null")
            val memberVal = member ?: throw IllegalArgumentException("member must not be null")
            val contentVal = content ?: throw IllegalArgumentException("content must not be null")
            val ratingVal = rating ?: throw IllegalArgumentException("rating must not be null")
            return Review(bookVal, memberVal, contentVal, ratingVal)
        }
    }
}