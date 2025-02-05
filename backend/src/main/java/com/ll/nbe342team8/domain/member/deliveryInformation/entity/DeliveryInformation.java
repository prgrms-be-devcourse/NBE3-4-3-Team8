package com.ll.nbe342team8.domain.member.deliveryInformation.entity;

import com.ll.nbe342team8.domain.member.deliveryInformation.dto.DeliveryInformationDto;
import com.ll.nbe342team8.domain.member.deliveryInformation.dto.ReqDeliveryInformationDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import com.ll.nbe342team8.standard.util.Ut;
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

    public DeliveryInformation(ReqDeliveryInformationDto dto, Member member) {
        this.addressName = Ut.XSSSanitizer.sanitize(dto.getAddressName());
        this.postCode = Ut.XSSSanitizer.sanitize(dto.getPostCode());
        this.detailAddress = Ut.XSSSanitizer.sanitize(dto.getDetailAddress());
        this.recipient = Ut.XSSSanitizer.sanitize(recipient);
        this.phone = Ut.XSSSanitizer.sanitize(dto.getPhone());
        this.isDefaultAddress=dto.getIsDefaultAddress();
        this.member=member;
    }

    public void updateDeliveryInfo(ReqDeliveryInformationDto dto) {
        this.addressName = Ut.XSSSanitizer.sanitize(dto.getAddressName());
        this.postCode = Ut.XSSSanitizer.sanitize(dto.getPostCode());
        this.detailAddress = Ut.XSSSanitizer.sanitize(dto.getDetailAddress());
        this.recipient = Ut.XSSSanitizer.sanitize(recipient);
        this.phone = Ut.XSSSanitizer.sanitize(dto.getPhone());
        this.isDefaultAddress=dto.getIsDefaultAddress();

    }

}
