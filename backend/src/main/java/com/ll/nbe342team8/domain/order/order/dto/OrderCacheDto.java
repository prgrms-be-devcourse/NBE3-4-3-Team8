package com.ll.nbe342team8.domain.order.order.dto;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCacheDto implements Serializable {
    private Long memberId;
    private String tossOrderId;
    private String fullAddress;
    private String postCode;
    private String recipient;
    private String phone;
    private String paymentMethod;
    private Long totalPrice;
    private List<DetailOrderCacheDto> detailOrders;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailOrderCacheDto implements Serializable {
        private Long bookId;
        private int quantity;
    }

    public static OrderCacheDto from(Member member, OrderRequestDto dto, Long totalPrice, List<DetailOrderCacheDto> detailOrders) {
        return OrderCacheDto.builder()
                .memberId(member.getId())
                .tossOrderId(dto.tossOrderId())
                .fullAddress(dto.fullAddress())
                .postCode(dto.postCode())
                .recipient(dto.recipient())
                .phone(dto.phone())
                .paymentMethod(dto.paymentMethod())
                .totalPrice(totalPrice)
                .detailOrders(detailOrders)
                .build();
    }
}
