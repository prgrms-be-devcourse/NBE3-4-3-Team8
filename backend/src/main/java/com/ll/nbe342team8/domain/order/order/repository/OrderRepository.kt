package com.ll.nbe342team8.domain.order.order.repository

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.order.order.entity.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface OrderRepository : JpaRepository<Order, Long> {

    fun findByMember(member: Member, pageable: Pageable): Page<Order>
    fun findByIdAndMember(orderId: Long, member: Member): Optional<Order>
}