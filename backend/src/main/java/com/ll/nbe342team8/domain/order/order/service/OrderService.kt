package com.ll.nbe342team8.domain.order.order.service

import com.ll.nbe342team8.domain.book.book.service.BookService
import com.ll.nbe342team8.domain.cart.service.CartService
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO
import com.ll.nbe342team8.domain.order.order.entity.Order
import com.ll.nbe342team8.domain.order.order.repository.OrderRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val detailOrderRepository: DetailOrderRepository,
    private val memberRepository: MemberRepository,
    private val cartService: CartService,
    private val bookService: BookService
) {

    @Transactional(readOnly = true)
    fun getOrdersByMember(member: Member, pageable: Pageable): Page<OrderDTO> {
        val ordersPage = orderRepository.findByMember(member, pageable)
        if (ordersPage.isEmpty) {
            throw IllegalArgumentException("주문이 존재하지 않습니다.")
        }

        return ordersPage.map { order ->
            val detailOrder = order.detailOrders.firstOrNull()
            val coverImage = detailOrder?.book?.coverImage ?: ""
            val title = detailOrder?.book?.title ?: "제목 없음"
            val Id = detailOrder?.book?.id ?: 0L
            OrderDTO(
                order.id ?: 0L,
                order.orderStatus.name,
                order.totalPrice,
                order.createDate,
                coverImage,
                title,
                Id
            )
        }
    }

    @Transactional
    fun deleteOrder(orderId: Long, member: Member) {
        val order = orderRepository.findByIdAndMember(orderId, member)
            .orElseThrow { IllegalArgumentException("해당 주문이 존재하지 않거나 권한이 없습니다.") }
        if (order.orderStatus != Order.OrderStatus.COMPLETE) {
            throw IllegalStateException("주문이 완료되지 않아 삭제할 수 없습니다.")
        }
        detailOrderRepository.deleteByOrderId(orderId)
        orderRepository.delete(order)
    }
}