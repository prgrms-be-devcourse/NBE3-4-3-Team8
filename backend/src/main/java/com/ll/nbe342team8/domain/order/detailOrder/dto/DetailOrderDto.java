package com.ll.nbe342team8.domain.order.detailOrder.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetailOrderDto {
    private Long orderId;
    private Long bookId;
    private int bookQuantity;
    private int deliveryStatus;


    
}
