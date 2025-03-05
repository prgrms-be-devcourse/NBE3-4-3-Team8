package com.ll.nbe342team8.domain.order.order.entity

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder
import com.ll.nbe342team8.global.jpa.entity.BaseTime
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "orders")
class Order(
	@ManyToOne val member: Member,
	@Enumerated(EnumType.STRING) val orderStatus: OrderStatus,
	val totalPrice: Long,
	val fullAddress: String? = null,
	val postCode: String? = null,
	val recipient: String? = null,
	val phone: String? = null,
	val paymentMethod: String? = null,
	@OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
	val detailOrders: MutableList<DetailOrder> = mutableListOf()  // 기본값 설정
) : BaseTime() {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null

	enum class OrderStatus {
		ORDERED,
		CANCELLED,
		COMPLETE
	}

	// 기본 생성자 추가
	constructor() : this(
		member = Member(), // 기본값을 설정해야 함 (예: Member()는 더미 객체일 수 있음)
		orderStatus = OrderStatus.ORDERED, // 기본 상태 설정
		totalPrice = 0L // 기본 가격 설정
	)
}