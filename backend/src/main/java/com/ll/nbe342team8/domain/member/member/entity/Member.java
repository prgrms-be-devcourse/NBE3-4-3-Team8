package com.ll.nbe342team8.domain.member.member.entity;

import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    private String name; // 사용자 이름

    private String phoneNumber; // 전화번호

    @Enumerated(EnumType.STRING)
    private MemberType memberType; // 사용자 역할(사용자, 관리자)

    private String oauthId;

    private String email; // 사용자 이메일

    private String password;

    private String username;



    // Enum 사용자 역할
    public enum MemberType {
        USER,
        ADMIN
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryInformation> deliveryInformations;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER)
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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public String getOauthId() {
        return oauthId;
    }
}

