package com.ll.nbe342team8.domain.order.detailOrder.dto;

import com.ll.nbe342team8.domain.order.detailOrder.entity.DeliveryStatus;

public record DetailOrderDto(
        Long orderId,
        Long bookId,
        int bookQuantity,
        DeliveryStatus deliveryStatus
) {
}