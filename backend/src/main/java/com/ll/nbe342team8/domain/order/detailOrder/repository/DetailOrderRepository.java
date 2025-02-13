package com.ll.nbe342team8.domain.order.detailOrder.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import com.ll.nbe342team8.domain.member.member.entity.Member;

@Repository
public interface DetailOrderRepository extends JpaRepository<DetailOrder, Long> {

	List<DetailOrder> findByOrderId(Long orderId);

	Page<DetailOrder> findByOrderId(Long orderId, Pageable pageable);

	void deleteByOrderId(Long orderId);

}