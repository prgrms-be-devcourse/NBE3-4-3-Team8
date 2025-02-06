package com.ll.nbe342team8.domain.member.member.entity;

import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTime {

    @Column(name = "name")
    private String name; // 사용자 이름

    @Column(name = "phone_number")
    private String phoneNumber; // 전화번호

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type")
    private MemberType memberType; // 사용자 역할(사용자, 관리자)

    @Column(name="oauth_id")
    private Long oauthId;

    @Column(name = "email")
    private String email; // 소셜 로그인 ID

    // Enum 사용자 역할
    public enum MemberType {
        USER,
        ADMIN
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryInformation> deliveryInformations;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Cart> carts;


    public void updateMemberInfo(PutReqMemberMyPageDto dto) {
        this.name = dto.getName();
        this.phoneNumber = dto.getPhoneNumber();

    }

    public void addDeliveryInformaiton(DeliveryInformation deliveryInformation) {
        this.deliveryInformations.add(deliveryInformation);
    }

    public void convertFalseDeliveryInformaitonsIsDefaultAddress() {
        deliveryInformations.forEach(info -> info.setIsDefaultAddress(false));
    }

    public void deleteDeliveryInformaiton(Long id) {
        deliveryInformations.removeIf(deliveryInfo -> deliveryInfo.getId().equals(id));
    }
}
