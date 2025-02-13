package com.ll.nbe342team8.domain.order.detailOrder.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ll.nbe342team8.domain.order.detailOrder.dto.AdminDetailOrderDTO;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DeliveryStatus;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDetailOrderService {

	private final DetailOrderRepository detailOrderRepository;

	// 상세 주문 내역 조회
	public Page<AdminDetailOrderDTO> getDetailsByOrderId(Long orderId, int page, int size, String sort) {
		Sort sorting;

		if ("book-title".equals(sort)) {
			sorting = Sort.by(Sort.Direction.ASC, "book.title");   // 책 제목 오름차순
		} else if ("book-title,desc".equals(sort)) {
			sorting = Sort.by(Sort.Direction.DESC, "book.title");  // 책 제목 내림차순
		} else {
			sorting = Sort.by(Sort.Direction.ASC, "book.title");   // 기본 정렬
		}

		Pageable pageable = PageRequest.of(page, size, sorting);

		return detailOrderRepository.findByOrderId(orderId, pageable)
				.map(AdminDetailOrderDTO::fromEntity);
	}

	// 상세 주문 배송 상태 수정
	@Transactional
	public AdminDetailOrderDTO updateDetailStatus(Long detailOrderId, DeliveryStatus status) {
		DetailOrder detailOrder = detailOrderRepository.findById(detailOrderId)
				.orElseThrow(() -> new EntityNotFoundException("해당 상세 주문을 찾을 수 없습니다."));

		detailOrder.setDeliveryStatus(status); // 배송 상태 변경
		detailOrderRepository.save(detailOrder); // 변경사항 저장

		return AdminDetailOrderDTO.fromEntity(detailOrder); // 변경된 값 반환
	}
}