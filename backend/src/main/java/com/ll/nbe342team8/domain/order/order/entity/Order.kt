package com.ll.nbe342team8.domain.order.order.entity

import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder
import com.ll.nbe342team8.global.jpa.entity.BaseTime
import jakarta.persistence.*

@Entity
@Table(name = "orders")
class Order private constructor(
    @ManyToOne val member: Member,
    @Enumerated(EnumType.STRING) val orderStatus: OrderStatus,
    val totalPrice: Long,
    val fullAddress: String? = null,
    val postCode: String? = null,
    val recipient: String? = null,
    val phone: String? = null,
    val paymentMethod: String? = null,
    val tossOrderId: String? = null,
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val detailOrders: MutableList<DetailOrder> = mutableListOf()
) : BaseTime() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    enum class OrderStatus {
        ORDERED,
        CANCELLED,
        COMPLETE
    }

    // 빌더 패턴을 위한 Builder 클래스
    class Builder {
        private var member: Member? = null
        private var orderStatus: OrderStatus = OrderStatus.ORDERED
        private var totalPrice: Long = 0
        private var fullAddress: String? = null
        private var postCode: String? = null
        private var recipient: String? = null
        private var phone: String? = null
        private var paymentMethod: String? = null
        private var tossOrderId: String? = null
        private var detailOrders: MutableList<DetailOrder> = mutableListOf()

        fun member(member: Member) = apply { this.member = member }
        fun orderStatus(orderStatus: OrderStatus) = apply { this.orderStatus = orderStatus }
        fun totalPrice(totalPrice: Long) = apply { this.totalPrice = totalPrice }
        fun fullAddress(fullAddress: String?) = apply { this.fullAddress = fullAddress }
        fun postCode(postCode: String?) = apply { this.postCode = postCode }
        fun recipient(recipient: String?) = apply { this.recipient = recipient }
        fun phone(phone: String?) = apply { this.phone = phone }
        fun paymentMethod(paymentMethod: String?) = apply { this.paymentMethod = paymentMethod }
        fun tossOrderId(tossOrderId: String?) = apply { this.tossOrderId = tossOrderId }
        fun detailOrders(detailOrders: MutableList<DetailOrder>) = apply { this.detailOrders = detailOrders }

        fun build(): Order {
            requireNotNull(member) { "Member cannot be null" }

            return Order(
                member = member!!,
                orderStatus = orderStatus,
                totalPrice = totalPrice,
                fullAddress = fullAddress,
                postCode = postCode,
                recipient = recipient,
                phone = phone,
                paymentMethod = paymentMethod,
                tossOrderId = tossOrderId,
                detailOrders = detailOrders
            )
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}
