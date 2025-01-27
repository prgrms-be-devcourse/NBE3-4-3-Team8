package com.ll.nbe342team8.domain.order.detailOrder.repository;

import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailOrderRepository extends JpaRepository<DetailOrder, Long> {
}
