package com.ll.nbe342team8.domain.order.detailOrder.dto;

import java.time.LocalDateTime;

import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;

public record AdminDetailOrderDTO(
		Long id,                      // 상세 주문 번호
		Long orderId,                 // 주문 번호
		LocalDateTime modifyDate,     // 수정날짜
		String bookTitle,             // 책 제목
		int bookQuantity,             // 구입수량
		String deliveryStatus         // 개별 배송상태
) {
	public static AdminDetailOrderDTO fromEntity(DetailOrder detailOrder) {
		if (detailOrder == null || detailOrder.getOrder() == null || detailOrder.getBook() == null) {
			throw new IllegalArgumentException("주문, 책이 비어있습니다.");
		}
		return new AdminDetailOrderDTO(
				detailOrder.getId(),
				detailOrder.getOrder().getId(),
				detailOrder.getModifyDate(),
				detailOrder.getBook().getTitle(),
				detailOrder.getBookQuantity(),
				detailOrder.getDeliveryStatus().name()
		);
	}
}
