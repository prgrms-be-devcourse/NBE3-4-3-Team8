package com.ll.nbe342team8.domain.order.order.repository;

import com.ll.nbe342team8.domain.order.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
