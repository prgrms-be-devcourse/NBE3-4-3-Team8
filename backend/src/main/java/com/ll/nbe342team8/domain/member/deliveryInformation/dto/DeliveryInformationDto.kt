package com.ll.nbe342team8.domain.member.deliveryInformation.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation

data class DeliveryInformationDto(
    val id: Long?,
    val addressName: String,
    val postCode: String,
    val detailAddress: String,
    val recipient: String,
    val phone: String,
    @get:JsonProperty("isDefaultAddress")
    val isDefaultAddress: Boolean
) {
    constructor(deliveryInformation: DeliveryInformation) : this(
        deliveryInformation.id,
        deliveryInformation.addressName,
        deliveryInformation.postCode,
        deliveryInformation.detailAddress,
        deliveryInformation.recipient,
        deliveryInformation.phone,
        deliveryInformation.isDefaultAddress
    )
}