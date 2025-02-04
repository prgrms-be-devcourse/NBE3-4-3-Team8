package com.ll.nbe342team8.domain.member.deliveryInformation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryInformationDto {

    @JsonProperty("id")
    Long id;

    @JsonProperty("addressName")
    String addressName;

    @JsonProperty("postCode")
    String postCode;

    @JsonProperty("detailAddress")
    String detailAddress;

    @JsonProperty("recipient")
    String recipient;

    @JsonProperty("phone")
    String phone;

    @JsonProperty("isDefaultAddress")
    Boolean isDefaultAddress;

    public DeliveryInformationDto(DeliveryInformation deliveryInformation) {

        this.id=deliveryInformation.getId();
        this.addressName=deliveryInformation.getAddressName();
        this.postCode=deliveryInformation.getPostCode();
        this.detailAddress=deliveryInformation.getDetailAddress();
        this.recipient=deliveryInformation.getRecipient();
        this.phone=deliveryInformation.getPhone();
        this.isDefaultAddress= deliveryInformation.getIsDefaultAddress();

    }



}
