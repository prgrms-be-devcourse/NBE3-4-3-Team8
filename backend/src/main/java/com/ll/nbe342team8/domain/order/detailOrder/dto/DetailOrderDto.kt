package com.ll.nbe342team8.domain.order.detailOrder.dto

data class DetailOrderDto(
    val orderId: Long,
    val bookTitle: String,
    val bookQuantity: Int,
    val totalPrice: Double,
    val deliveryStatus: String,
    val coverImage: String?,
    val recipient: String,
    val phone: String,
    val fullAddress: String
)