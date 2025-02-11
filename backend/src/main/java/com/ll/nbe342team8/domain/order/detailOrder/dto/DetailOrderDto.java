package com.ll.nbe342team8.domain.order.detailOrder.dto;


import com.ll.nbe342team8.domain.order.detailOrder.entity.DeliveryStatus;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record DetailOrderDto(Long orderId, Long bookId, int bookQuantity, DetailOrder.DeliveryStatus deliveryStatus) {
}