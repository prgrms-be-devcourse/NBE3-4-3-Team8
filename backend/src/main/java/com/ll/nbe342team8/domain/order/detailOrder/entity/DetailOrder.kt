package com.ll.nbe342team8.domain.order.detailOrder.entity

import com.ll.nbe342team8.domain.book.book.entity.Book
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.order.order.entity.Order
import com.ll.nbe342team8.global.jpa.entity.BaseTime
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
) : BaseTime()