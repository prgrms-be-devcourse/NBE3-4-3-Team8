package com.ll.nbe342team8.domain.member.deliveryInformation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record DeliveryInformationDto(
        Long id,
        String addressName,
        String postCode,
        String detailAddress,
        String recipient,
        String phone,
        Boolean isDefaultAddress
) {
    public DeliveryInformationDto(DeliveryInformation deliveryInformation) {
        this(
                deliveryInformation.getId(),
                deliveryInformation.getAddressName(),
                deliveryInformation.getPostCode(),
                deliveryInformation.getDetailAddress(),
                deliveryInformation.getRecipient(),
                deliveryInformation.getPhone(),
                deliveryInformation.getIsDefaultAddress()
        );
    }
}