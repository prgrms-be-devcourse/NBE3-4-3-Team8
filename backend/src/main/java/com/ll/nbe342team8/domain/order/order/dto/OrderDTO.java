
package com.ll.nbe342team8.domain.order.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDTO {
    private Long memberId;
    private String orderStatus;
    private long totalPrice;
}