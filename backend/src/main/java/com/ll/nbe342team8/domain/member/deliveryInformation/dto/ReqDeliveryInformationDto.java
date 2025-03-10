package com.ll.nbe342team8.domain.member.deliveryInformation.dto;

import jakarta.validation.constraints.*;



public record ReqDeliveryInformationDto(
        Long id,

        @NotBlank(message = "공백은 허용하지 않습니다.")
        @Size(max = 50)
        String addressName,

        @NotBlank(message = "공백은 허용하지 않습니다.")
        @Size(max = 10)
        String postCode,

        @NotBlank(message = "공백은 허용하지 않습니다.")
        @Size(max = 100)
        String detailAddress,

        @NotBlank(message = "공백은 허용하지 않습니다.")
        @Size(max = 50)
        String recipient,


        @NotBlank(message = "공백은 허용하지 않습니다.")
        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다. (010-XXXX-XXXX)")
        String phone,

        @NotNull
        Boolean isDefaultAddress
) {}