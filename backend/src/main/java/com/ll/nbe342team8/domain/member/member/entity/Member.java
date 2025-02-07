package com.ll.nbe342team8.domain.member.member.entity;

import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
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
    private String oauthId;

    @Column(name = "email")
    private String email; // 사용자 이메일

    @Column(name = "password", nullable = false)
    private String password;


    // Enum 사용자 역할
    public enum MemberType {
        USER,
        ADMIN
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryInformation> deliveryInformations;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Cart> carts;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Question> questions;


    public void updateMemberInfo(PutReqMemberMyPageDto dto) {
        this.name = dto.getName();
        this.phoneNumber = dto.getPhoneNumber();

    }

    public void addDeliveryInformation(DeliveryInformation deliveryInformation) {
        this.deliveryInformations.add(deliveryInformation);
    }

    public void convertFalseDeliveryInformationsIsDefaultAddress() {
        deliveryInformations.forEach(info -> info.setIsDefaultAddress(false));
    }

    

    public String getUsername() {
        return oauthId;
    }
    public String getNickname() {
        return name;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
}
