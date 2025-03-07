package com.ll.nbe342team8.domain.order.order.service

import com.ll.nbe342team8.domain.book.book.service.BookService
import com.ll.nbe342team8.domain.cart.dto.CartResponseDto
import com.ll.nbe342team8.domain.cart.entity.Cart
import com.ll.nbe342team8.domain.cart.service.CartService
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository
import com.ll.nbe342team8.domain.order.detailOrder.entity.DeliveryStatus
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO
import com.ll.nbe342team8.domain.order.order.dto.OrderRequestDto
import com.ll.nbe342team8.domain.order.order.dto.PaymentResponseDto
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

    @Transactional
    fun createOrder(member: Member, orderRequestDTO: OrderRequestDto): Order {
        val cartList = cartService.findCartByMember(member)

        val order = Order(
            member = member,
            orderStatus = Order.OrderStatus.ORDERED,
            fullAddress = orderRequestDTO.fullAddress(),
            postCode = orderRequestDTO.postCode(),
            phone = orderRequestDTO.phone(),
            recipient = orderRequestDTO.recipient(),
            paymentMethod = orderRequestDTO.paymentMethod(),
            totalPrice = calculateTotalPriceSales(cartList)
        )

        orderRepository.save(order)

        val detailOrders = cartList.map { cart ->
            DetailOrder(
                order = order,
                deliveryStatus = DeliveryStatus.PENDING,
                book = cart.book,
                bookQuantity = cart.quantity,
                member = member
            )
        }

        detailOrderRepository.saveAll(detailOrders)

        cartService.deleteProduct(member) // 주문 완료 후 장바구니 비우기

        return order
    }

    @Transactional
    fun createFastOrder(member: Member, orderRequestDTO: OrderRequestDto): Order {
        val cartList = cartService.findCartByMember(member)
        val order = Order(
            member = member,
            orderStatus = Order.OrderStatus.ORDERED,
            fullAddress = orderRequestDTO.fullAddress(),
            postCode = orderRequestDTO.postCode(),
            phone = orderRequestDTO.phone(),
            recipient = orderRequestDTO.recipient(),
            paymentMethod = orderRequestDTO.paymentMethod(),
            totalPrice = calculateTotalPriceSales(cartList)
        )
        orderRepository.save(order)

        val detailOrders = cartList.map { cart ->
            DetailOrder(
                order = order,
                deliveryStatus = DeliveryStatus.PENDING,
                book = cart.book,
                bookQuantity = cart.quantity,
                member = member
            )
        }
        detailOrderRepository.saveAll(detailOrders)

        cartService.deleteProduct(member) // 장바구니 비우기
        return order
    }

    @Transactional
    fun createFastOrder(member: Member, orderRequestDTO: OrderRequestDto, bookId: Long, quantity: Int): Order {
        val book = bookService.getBookById(bookId)
        val totalPrice = book.pricesSales * quantity.toLong()

        val order = Order(
            member = member,
            orderStatus = Order.OrderStatus.ORDERED,
            fullAddress = orderRequestDTO.fullAddress(),
            postCode = orderRequestDTO.postCode(),
            phone = orderRequestDTO.phone(),
            recipient = orderRequestDTO.recipient(),
            paymentMethod = orderRequestDTO.paymentMethod(),
            totalPrice = totalPrice
        )
        orderRepository.save(order)

        val detailOrder = DetailOrder(
            order = order,
            deliveryStatus = DeliveryStatus.PENDING,
            book = book,
            bookQuantity = quantity,
            member = member
        )
        detailOrderRepository.save(detailOrder)

        return order
    }

    private fun calculateTotalPriceSales(cartList: List<Cart>): Long {
        return cartList.sumOf { (it.book.pricesSales * it.quantity).toLong() }
    }

    private fun calculateTotalPriceStandard(cartList: List<Cart>): Long {
        return cartList.sumOf { (it.book.priceStandard * it.quantity).toLong() }
    }

    fun createPaymentInfo(member: Member): PaymentResponseDto {
        val cartList = cartService.findCartByMember(member)
        val cartResponseDtoList = cartList.map { CartResponseDto.from(it) }
        val totalPriceSales = calculateTotalPriceSales(cartList)
        val totalPriceStandard = calculateTotalPriceStandard(cartList)
        return PaymentResponseDto(cartResponseDtoList, totalPriceStandard, totalPriceSales)
    }
}