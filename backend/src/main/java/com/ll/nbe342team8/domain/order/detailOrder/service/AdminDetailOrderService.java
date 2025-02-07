package com.ll.nbe342team8.domain.order.detailOrder.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ll.nbe342team8.domain.order.detailOrder.dto.AdminDetailOrderDTO;
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDetailOrderService {

	private final DetailOrderRepository detailOrderRepository;

	public Page<AdminDetailOrderDTO> getDetailsByOrderIdForAdmin(Long orderId, int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

		return detailOrderRepository.findByOrderId(orderId, pageable)
				.map(AdminDetailOrderDTO::fromEntity);
	}
}
