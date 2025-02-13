package com.ll.nbe342team8.domain.order.order.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.nbe342team8.domain.order.order.dto.AdminOrderDTO;
import com.ll.nbe342team8.domain.order.order.service.AdminOrderService;
import com.ll.nbe342team8.domain.order.type.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/orders")
@Tag(name = "Order", description = "관리자 주문 API")
@RequiredArgsConstructor
public class AdminOrderController {

	private final AdminOrderService adminOrderService;

	@GetMapping
	@Operation(summary = "전체 회원 주문 조회")
	public Page<AdminOrderDTO> getAllOrders(@RequestParam(defaultValue = "0") int page,
											@RequestParam(defaultValue = "10") int pageSize,
											@RequestParam(defaultValue = "ORDER_DATE") SortType sortType) {
		return adminOrderService.getAllOrders(page, pageSize, sortType);
	}
}
