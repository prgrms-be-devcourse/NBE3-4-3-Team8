package com.ll.nbe342team8.domain.order.order.dto

import java.time.LocalDateTime

data class OrderDTO(
    val orderId: Long,
    val orderStatus: String,
    val totalPrice: Long,
    val createDate: LocalDateTime,
    val coverImage: String,
    val title: String,
    val Id: Long
)