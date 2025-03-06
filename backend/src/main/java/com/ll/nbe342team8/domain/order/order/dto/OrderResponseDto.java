package com.ll.nbe342team8.domain.order.order.dto;

import com.ll.nbe342team8.domain.order.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String orderStatus;
    private Long totalPrice;
    private String recipient;
    private String postCode;
    private String fullAddress;
    private String phone;
    private String paymentMethod;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus().name();
        this.totalPrice = order.getTotalPrice();
        this.recipient = order.getRecipient();
        this.postCode = order.getPostCode();
        this.fullAddress = order.getFullAddress();
        this.phone = order.getPhone();
        this.paymentMethod = order.getPaymentMethod();
    }

    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(order);
    }
}