package com.ll.nbe342team8.domain.order.detailOrder.repository;

import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailOrderRepository extends JpaRepository<DetailOrder, Long> {
    List<DetailOrder> findByOrderId(Long orderId);
    void deleteByOrderId(Long orderId);
}
