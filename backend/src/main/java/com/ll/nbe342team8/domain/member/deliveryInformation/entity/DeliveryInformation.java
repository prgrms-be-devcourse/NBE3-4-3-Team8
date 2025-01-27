package com.ll.nbe342team8.domain.member.deliveryInformation.entity;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInformation extends BaseTime {

    private String addressName;
    
    private String postCode;
    
    private String detailAddress;
    
    private boolean isDefaultAddress;
    
    private String recipient;
    
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;




}
