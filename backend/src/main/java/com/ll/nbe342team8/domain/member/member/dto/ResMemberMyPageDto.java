package com.ll.nbe342team8.domain.member.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.nbe342team8.domain.member.deliveryInformation.dto.DeliveryInformationDto;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public record ResMemberMyPageDto(
        String name,
        String phoneNumber,
        List<DeliveryInformationDto> deliveryInformationDtos
) {
    public ResMemberMyPageDto(Member member) {
        this(
                member.getName(),
                member.getPhoneNumber(),
                member.getDeliveryInformations().stream()
                        .sorted(Comparator
                                .comparing((DeliveryInformation di) -> di.getIsDefaultAddress() != null && di.getIsDefaultAddress() ? 0 : 1) // 기본 배송지를 우선 정렬
                                .thenComparing(DeliveryInformation::getAddressName, Comparator.nullsLast(Comparator.naturalOrder()))) // addressName 오름차순 정렬
                        .map(DeliveryInformationDto::new) // DTO 변환
                        .collect(Collectors.toList())
        );
    }
}
