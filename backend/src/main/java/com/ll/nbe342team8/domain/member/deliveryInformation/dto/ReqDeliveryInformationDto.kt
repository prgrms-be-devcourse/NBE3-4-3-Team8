package com.ll.nbe342team8.domain.member.deliveryInformation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ReqDeliveryInformationDto(
    val id: Long? = null,

    @field:Size(max = 50)
    val addressName: String = "",

    @field:Size(max = 10)
    val postCode: String = "",

    @field:Size(max = 100)
    val detailAddress: String = "",

    @field:Size(max = 50)
    val recipient: String = "",

    @field:Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다. (010-XXXX-XXXX)")
    val phone: String = "",

    @JsonProperty("isDefaultAddress")
    val isDefaultAddress: Boolean = false
)