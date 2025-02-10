package com.ll.nbe342team8.domain.member.deliveryInformation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqDeliveryInformationDto {

    @JsonProperty("id")
    Long id;

    @NotBlank(message = "공백은 허용하지 않습니다.")
    @JsonProperty("addressName")
    String addressName;

    @NotBlank(message = "공백은 허용하지 않습니다.")
    @JsonProperty("postCode")
    String postCode;

    @NotBlank(message = "공백은 허용하지 않습니다.")
    @JsonProperty("detailAddress")
    String detailAddress;

    @NotBlank(message = "공백은 허용하지 않습니다.")
    @JsonProperty("recipient")
    String recipient;

    @NotBlank(message = "공백은 허용하지 않습니다.")
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다. (010-XXXX-XXXX)")
    @JsonProperty("phone")
    String phone;

    @NotNull
    @JsonProperty("isDefaultAddress")
    Boolean isDefaultAddress;


}
