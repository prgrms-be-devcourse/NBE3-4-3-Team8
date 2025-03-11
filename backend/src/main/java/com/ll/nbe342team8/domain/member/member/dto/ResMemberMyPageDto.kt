package com.ll.nbe342team8.domain.member.member.dto

import com.ll.nbe342team8.domain.member.deliveryInformation.dto.DeliveryInformationDto
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation
import com.ll.nbe342team8.domain.member.member.entity.Member
import java.util.stream.Collectors

data class ResMemberMyPageDto(
    val name: String?,
    val phoneNumber: String?,
    val deliveryInformationDtos: List<DeliveryInformationDto>
) {
    constructor(member: Member) : this(
        name = member.name,
        phoneNumber = member.phoneNumber,
        deliveryInformationDtos = member.deliveryInformations
            .sortedWith(
                compareBy<DeliveryInformation> { if (it.isDefaultAddress == true) 0 else 1 }
                    .thenBy(nullsLast()) { it.addressName }
            )
            .map { DeliveryInformationDto(it) }
    )
}