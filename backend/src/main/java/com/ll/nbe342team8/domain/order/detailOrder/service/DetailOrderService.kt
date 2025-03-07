package com.ll.nbe342team8.domain.order.detailOrder.service

import com.ll.nbe342team8.domain.jwt.AuthService
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.order.detailOrder.dto.DetailOrderDto
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DetailOrderService(
    private val detailOrderRepository: DetailOrderRepository,
    private val authService: AuthService
) {

    @Transactional
    // 주문상세조회
    fun getDetailOrdersByOrderIdAndMember(orderId: Long, member: Member): List<DetailOrderDto> {

        val detailOrders: List<DetailOrder> = detailOrderRepository.findByOrderId(orderId)

        // 주문 상세 정보를 DetailOrderDto로 변환하여 반환
        return detailOrders.map { detailOrder ->
            DetailOrderDto(
                orderId = detailOrder.order.id!!,
                bookId = detailOrder.book.id!!,
                bookQuantity = detailOrder.bookQuantity,
                deliveryStatus = detailOrder.deliveryStatus,
                coverImage = detailOrder.book.coverImage
            )
        }
    }
}