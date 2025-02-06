package com.ll.nbe342team8.domain.order.type;

import org.springframework.data.domain.Sort;

public enum SortType {
	// 주문 정렬 기준 (관리자)
	ORDER_DATE("createDate", Sort.Direction.DESC),     // 주문일자 최신순
	TOTAL_PRICE("totalPrice", Sort.Direction.DESC),    // 주문 총액 높은순
	STATUS("orderStatus", Sort.Direction.ASC);         // 주문 상태별 정렬

	private final String field;
	private final Sort.Direction direction;

	SortType(String field, Sort.Direction direction) {
		this.field = field;
		this.direction = direction;
	}

	public Sort.Order getOrder() {
		return new Sort.Order(direction, field);
	}
}
