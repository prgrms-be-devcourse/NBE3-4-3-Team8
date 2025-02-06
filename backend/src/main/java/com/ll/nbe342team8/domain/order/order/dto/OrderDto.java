package com.ll.nbe342team8.domain.order.order.dto;

import com.ll.nbe342team8.domain.order.order.entity.Order;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long orderId;
    private Long memberId;
    private String orderStatus;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long totalPrice;

    public static OrderDto of(Order order) {
        return OrderDto.builder()
                .orderId(order.getId())
                .memberId(order.getMember().getId())
                .orderStatus(String.valueOf(order.getOrderType()))
                .createDate(order.getCreateDate())
                .modifyDate(order.getModifyDate())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
