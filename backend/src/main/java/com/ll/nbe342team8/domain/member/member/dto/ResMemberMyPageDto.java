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

@Getter
@Setter
@NoArgsConstructor
public class ResMemberMyPageDto {

    @JsonProperty("name")
    String name;

    @JsonProperty("phoneNumber")
    String phoneNumber;

    List<DeliveryInformationDto> deliveryInformationDtos;

    public ResMemberMyPageDto(Member member) {
        this.name=member.getName();
        this.phoneNumber= member.getPhoneNumber();
        List<DeliveryInformation> deliveryInformations=member.getDeliveryInformations();

        this.deliveryInformationDtos = deliveryInformations.stream()
                .sorted(Comparator.comparing((DeliveryInformation di) -> di.getIsDefaultAddress() != null && di.getIsDefaultAddress() ? 0 : 1) // true(0) 우선 정렬
                        .thenComparing(DeliveryInformation::getAddressName, Comparator.nullsLast(Comparator.naturalOrder()))) // 나머지는 addressName 오름차순
                .map(DeliveryInformationDto::new) // DTO 변환
                .collect(Collectors.toList());


    }
}
