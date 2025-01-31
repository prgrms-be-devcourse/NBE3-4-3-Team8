package com.ll.nbe342team8.domain.member.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
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

    @Column(name = "oauth_id")
    private Long oauthId; // 소셜 로그인 ID

    // Enum 사용자 역할
    public enum MemberType {
        USER,
        ADMIN
    }

    @JsonIgnoreProperties({"member"})
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Review> review;

//    @JsonIgnoreProperties({"member"})
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private Set<Cart> cart;
}
