package com.ll.nbe342team8.domain.order.detailOrder.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.nbe342team8.domain.order.detailOrder.dto.AdminDetailOrderDTO;
import com.ll.nbe342team8.domain.order.detailOrder.service.AdminDetailOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/orders")
@Tag(name = "OrderDetail", description = "관리자 주문 상세 API")
@RequiredArgsConstructor
public class AdminDetailOrderController {

	private final AdminDetailOrderService adminDetailOrderService;

	@GetMapping("/{orderId}")
	@Operation(summary = "주문 ID, 상세 주문 정보 조회 - 관리자")
	public Page<AdminDetailOrderDTO> getOrderDetails(
			@PathVariable Long orderId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "productName") String sort) {

		return adminDetailOrderService.getDetailsByOrderIdForAdmin(orderId, page, size, sort);
	}
}
