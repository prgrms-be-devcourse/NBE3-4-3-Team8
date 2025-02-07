package com.ll.nbe342team8.domain.order.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ll.nbe342team8.domain.order.detailOrder.dto.AdminDetailOrderDTO;
import com.ll.nbe342team8.domain.order.order.dto.AdminOrderDTO;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.domain.order.order.repository.OrderRepository;
import com.ll.nbe342team8.domain.order.type.SortType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

	private final OrderRepository orderRepository;

	public Page<AdminOrderDTO> getAllOrders(int page, int pageSize, SortType sortType) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortType.getOrder()));

		Page<Order> orders = orderRepository.findAll(pageable);

		return orders.map(order -> AdminOrderDTO.fromEntity(order, order.getDetailOrders()
				.stream()
				.map(AdminDetailOrderDTO::fromEntity)
				.toList()));
	}
}
