package com.ll.nbe342team8.domain.member.deliveryInformation.entity;

import com.ll.nbe342team8.domain.member.deliveryInformation.dto.DeliveryInformationDto;
import com.ll.nbe342team8.domain.member.deliveryInformation.dto.ReqDeliveryInformationDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import com.ll.nbe342team8.standard.util.Ut;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInformation extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    public Long id;

    public String addressName;

    public String postCode;

    public String detailAddress;

    public Boolean isDefaultAddress;

    public String recipient;

    public String phone;

    @ManyToOne
    public Member member;

    public DeliveryInformation(ReqDeliveryInformationDto dto, Member member) {
        this.addressName = dto.getAddressName();
        this.postCode = dto.getPostCode();
        this.detailAddress = dto.getDetailAddress();
        this.recipient = dto.getRecipient();
        this.phone = dto.getPhone();
        this.isDefaultAddress= dto.isDefaultAddress();
        this.member=member;
    }

    public void updateDeliveryInfo(ReqDeliveryInformationDto dto) {
        this.addressName = dto.getAddressName();
        this.postCode = dto.getPostCode();
        this.detailAddress = dto.getDetailAddress();
        this.recipient = dto.getRecipient();
        this.phone = dto.getPhone();
        this.isDefaultAddress= dto.isDefaultAddress();

    }

}