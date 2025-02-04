package com.ll.nbe342team8.domain.member.deliveryInformation.entity;

import com.ll.nbe342team8.domain.member.deliveryInformation.dto.DeliveryInformationDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInformation extends BaseTime {

    private String addressName;
    
    private String postCode;
    
    private String detailAddress;
    
    private Boolean isDefaultAddress;
    
    private String recipient;
    
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public DeliveryInformation(DeliveryInformationDto deliveryInformationDto,Member member) {
        this.addressName=deliveryInformationDto.getAddressName();
        this.postCode=deliveryInformationDto.getPostCode();
        this.detailAddress=deliveryInformationDto.getDetailAddress();
        this.isDefaultAddress=deliveryInformationDto.getIsDefaultAddress();
        this.recipient=deliveryInformationDto.getRecipient();
        this.phone=deliveryInformationDto.getPhone();
        this.member=member;
    }

    public void updateDeliveryInfo(DeliveryInformationDto dto) {
        if (dto.getAddressName() != null) this.addressName = dto.getAddressName();
        if (dto.getPostCode() != null) this.postCode = dto.getPostCode();
        if (dto.getDetailAddress() != null) this.detailAddress = dto.getDetailAddress();
        if (dto.getRecipient() != null) this.recipient = dto.getRecipient();
        if (dto.getPhone() != null) this.phone = dto.getPhone();
        if (dto.getIsDefaultAddress() !=null) this.isDefaultAddress=dto.getIsDefaultAddress();

    }

}
