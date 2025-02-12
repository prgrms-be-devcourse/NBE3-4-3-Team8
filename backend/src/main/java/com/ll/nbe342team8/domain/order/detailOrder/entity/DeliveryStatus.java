package com.ll.nbe342team8.domain.order.detailOrder.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DeliveryStatus {
	PENDING, // 대기중
	SHIPPING, // 배송중
	DELIVERED, // 배송완료
	RETURNED; // 반품

	@JsonCreator
	public static DeliveryStatus from(String value) {
		return DeliveryStatus.valueOf(value.toUpperCase());  // 소문자로 요청이 와도 변환 가능
	}
}

