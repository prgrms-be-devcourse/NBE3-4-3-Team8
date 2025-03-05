package com.ll.nbe342team8.domain.order.detailOrder.repository

import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder
import com.ll.nbe342team8.domain.member.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DetailOrderRepository : JpaRepository<DetailOrder, Long> {

	fun findByOrderId(orderId: Long): List<DetailOrder>

	fun findByOrderId(orderId: Long, pageable: Pageable): Page<DetailOrder>

	fun deleteByOrderId(orderId: Long)
}