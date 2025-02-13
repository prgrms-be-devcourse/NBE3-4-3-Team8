package com.ll.nbe342team8.domain.order.detailOrder.controller;

import com.ll.nbe342team8.domain.order.detailOrder.entity.DeliveryStatus;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.nbe342team8.domain.order.detailOrder.dto.AdminDetailOrderDTO;
import com.ll.nbe342team8.domain.order.detailOrder.dto.UpdateDetailOrderStatusRequest;
import com.ll.nbe342team8.domain.order.detailOrder.service.AdminDetailOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@Tag(name = "DetailOrder", description = "관리자 주문 상세 API")
@RequiredArgsConstructor
public class AdminDetailOrderController {

	private final AdminDetailOrderService adminDetailOrderService;

	@GetMapping("/orders/{orderId}/details")
	@Operation(summary = "상세 주문 조회", description = "상세 주문 ID를 이용해 정보를 조회합니다.")
	public Page<AdminDetailOrderDTO> getOrderDetails(
			@PathVariable Long orderId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "book-title") String sort) {

		return adminDetailOrderService.getDetailsByOrderId(orderId, page, size, sort);
	}

	@PatchMapping("/detail-orders/{detailOrderId}/status")
	@Operation(summary = "상세 주문 배송 상태 수정", description = "상세 주문 ID를 이용해 배송 상태를 변경합니다.")
	public ResponseEntity<AdminDetailOrderDTO> updateDetailStatus(
			@PathVariable(name = "detailOrderId") Long detailOrderId,
			@RequestBody UpdateDetailOrderStatusRequest request) {

		AdminDetailOrderDTO updatedOrder
				= adminDetailOrderService.updateDetailStatus(detailOrderId, DeliveryStatus.valueOf(request.getStatus().name()));

		return ResponseEntity.ok(updatedOrder);
	}
}