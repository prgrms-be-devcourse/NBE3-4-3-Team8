package com.ll.nbe342team8.domain.cart.repository;

import com.ll.nbe342team8.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
