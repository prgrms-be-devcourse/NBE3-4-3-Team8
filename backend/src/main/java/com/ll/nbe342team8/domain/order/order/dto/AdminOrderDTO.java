package com.ll.nbe342team8.domain.order.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ll.nbe342team8.domain.order.detailOrder.dto.AdminDetailOrderDTO;
import com.ll.nbe342team8.domain.order.order.entity.Order;

public record AdminOrderDTO(
		Long orderId,                               // 주문 번호
		LocalDateTime createdDate,                  // 주문 일시
		long totalPrice,                            // 총 주문 금액
		String status,                              // 주문 상태
		List<AdminDetailOrderDTO> detailOrders      // 상세 주문 내역
) {
	public static AdminOrderDTO fromEntity(Order order, List<AdminDetailOrderDTO> detailOrders) {
		return new AdminOrderDTO(
				order.getId(),
				order.getCreateDate(),
				order.getTotalPrice(),
				order.getOrderStatus().name(),
				detailOrders
		);
	}
}
