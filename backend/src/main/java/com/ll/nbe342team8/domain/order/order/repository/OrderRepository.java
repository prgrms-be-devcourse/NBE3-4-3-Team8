package com.ll.nbe342team8.domain.order.order.repository;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOauthId(String oauthId);

    Optional<Order> findByIdAndOauthId(Long orderId, String oauthId);


}

