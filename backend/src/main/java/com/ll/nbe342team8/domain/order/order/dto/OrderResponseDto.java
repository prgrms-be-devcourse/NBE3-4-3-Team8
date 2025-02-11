package com.ll.nbe342team8.domain.order.order.dto;

import com.ll.nbe342team8.domain.order.order.entity.Order;

public record OrderResponseDto(
        Long orderId
) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getId()
        );
    }
}
