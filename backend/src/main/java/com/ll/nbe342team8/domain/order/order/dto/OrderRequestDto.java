package com.ll.nbe342team8.domain.order.order.dto;

import jakarta.validation.constraints.Pattern;

public record OrderRequestDto(
        String postCode,      // 우편번호
        String fullAddress,   // 주소(도로명 + 상세주소)
        String recipient,     // 수령인

        @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
        String phone,         // 전화번호
        String paymentMethod  // 결제수단
) {
}
