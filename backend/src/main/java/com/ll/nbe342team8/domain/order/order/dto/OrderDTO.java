
package com.ll.nbe342team8.domain.order.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record OrderDTO(Long orderId, String orderStatus, long totalPrice) {
}