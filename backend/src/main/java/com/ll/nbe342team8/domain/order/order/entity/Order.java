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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Member member;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private long totalPrice;

	private String fullAddress;

	private String postCode;

	private String recipient;

	private String phone;

	private String paymentMethod;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<DetailOrder> detailOrders = new ArrayList<>();

	public enum OrderStatus {
		ORDERED,
		CANCELLED,
		COMPLETE
	}

	public Order(Member member, OrderStatus orderStatus, long totalPrice) {
		this.member = member;
		this.orderStatus = orderStatus;
		this.totalPrice = totalPrice;
		this.detailOrders = new ArrayList<>();
	}
}