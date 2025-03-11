package com.ll.nbe342team8.domain.order.detailOrder.entity

import com.ll.nbe342team8.domain.book.book.entity.Book
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.order.order.entity.Order
import com.ll.nbe342team8.global.jpa.entity.BaseTime
import jakarta.persistence.*

@Entity
class DetailOrder(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    val order: Order,

    @ManyToOne(fetch = FetchType.EAGER) //나중에 다시 변경
    val book: Book,

    @ManyToOne(fetch = FetchType.EAGER) //나중에 다시 변경
    val member: Member,

    val bookQuantity: Int,

    @Enumerated(EnumType.STRING)
    var deliveryStatus: DeliveryStatus
) : BaseTime() {

    // 빌더 패턴을 위한 Builder 클래스
    class Builder {
        private var id: Long? = null
        private var order: Order? = null
        private var book: Book? = null
        private var member: Member? = null
        private var bookQuantity: Int = 0
        private var deliveryStatus: DeliveryStatus? = null

        fun id(id: Long?) = apply { this.id = id }
        fun order(order: Order) = apply { this.order = order }
        fun book(book: Book) = apply { this.book = book }
        fun member(member: Member) = apply { this.member = member }
        fun bookQuantity(bookQuantity: Int) = apply { this.bookQuantity = bookQuantity }
        fun deliveryStatus(deliveryStatus: DeliveryStatus) = apply { this.deliveryStatus = deliveryStatus }

        fun build(): DetailOrder {
            requireNotNull(order) { "Order must not be null" }
            requireNotNull(book) { "Book must not be null" }
            requireNotNull(member) { "Member must not be null" }
            requireNotNull(deliveryStatus) { "DeliveryStatus must not be null" }

            return DetailOrder(
                id = id,
                order = order!!,
                book = book!!,
                member = member!!,
                bookQuantity = bookQuantity,
                deliveryStatus = deliveryStatus!!
            )
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}
