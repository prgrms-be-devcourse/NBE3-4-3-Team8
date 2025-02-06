package com.ll.nbe342team8.domain.order.detailOrder.dto;


import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetailOrderDto {
    private Long orderId;
    private Long bookId;
    private int bookQuantity;
    private DeliveryStatus deliveryStatus;


    
}
