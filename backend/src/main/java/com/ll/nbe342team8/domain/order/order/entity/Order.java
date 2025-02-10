package com.ll.nbe342team8.domain.order.order.entity;

import java.util.ArrayList;
import java.util.List;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status")
	private OrderStatus orderStatus;

	@Column(name = "total_price")
	private long totalPrice;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<DetailOrder> detailOrders = new ArrayList<>();

	public enum OrderStatus {
		ORDERED,
		DELIVERY,
		COMPLETE;
	}

	public Order(Member member, OrderStatus orderStatus, long totalPrice) {
		this.member = member;
		this.orderStatus = orderStatus;
		this.totalPrice = totalPrice;
		this.detailOrders = new ArrayList<>(); // 기본 빈 리스트
	}
}
