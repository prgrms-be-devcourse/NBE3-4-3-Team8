package com.ll.nbe342team8.domain.member.member.entity;

import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    @Column(name = "oauth_id")
    private Long oauthId; // 소셜 로그인 ID

    // Enum 사용자 역할
    public enum MemberType {
        USER,
        ADMIN
    }
}
