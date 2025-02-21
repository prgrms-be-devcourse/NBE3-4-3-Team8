package com.ll.nbe342team8.domain.cart.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ll.nbe342team8.domain.book.book.entity.Book
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.global.jpa.entity.BaseTime
import jakarta.persistence.*

@Entity
@Table(
    name = "cart",
    uniqueConstraints = [UniqueConstraint(name = "unique_member_book", columnNames = ["member_id", "book_id"])]
)
open class Cart(

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JsonIgnore
    var member: Member?,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JsonIgnore
    var book: Book,

    var quantity: Int

) : BaseTime() {

    constructor(): this(
        member = Member(),
        book = Book(),
        quantity = 99
    )

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set

    fun updateCart(quantity: Int) {
        this.quantity = quantity
    }

    override fun toString(): String {
        return "Cart{id=$id, member=$member, book=$book, quantity=$quantity}"
    }

    companion object {
        @JvmStatic
        fun create(book: Book, quantity: Int): Cart {
            return Cart(null, book, quantity)
        }

        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var member: Member? = null
        private var book: Book? = null
        private var quantity: Int? = null

        fun member(member: Member?) = apply { this.member = member }
        fun book(book: Book) = apply { this.book = book }
        fun quantity(quantity: Int) = apply { this.quantity = quantity }
        fun build(): Cart {
            val bookVal = book ?: throw IllegalArgumentException("book must not be null")
            val quantityVal = quantity ?: throw IllegalArgumentException("quantity must not be null")
            return Cart(member, bookVal, quantityVal)
        }
    }
}
