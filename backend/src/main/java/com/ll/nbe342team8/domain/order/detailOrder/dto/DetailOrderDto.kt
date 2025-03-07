package com.ll.nbe342team8.domain.order.detailOrder.dto

import com.ll.nbe342team8.domain.order.detailOrder.entity.DeliveryStatus

data class DetailOrderDto(
    val orderId: Long,
    val bookId: Long,
    val bookQuantity: Int,
    val deliveryStatus: DeliveryStatus,
    val  coverImage: String
)