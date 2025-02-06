package com.ll.nbe342team8.domain.oauth;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String oauthId;  // kakaoId를 oauthId로 변경
    private String name;
    private String email;   //

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String oauthId,
                           String name,
                           String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        // kakao_account에서 필요한 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .oauthId(String.valueOf(attributes.get("id")))  //
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .oauthId(oauthId)
                .name(name)
                .email(email)
                .phoneNumber("")
                .memberType(Member.MemberType.USER)
                .deliveryInformations(Collections.emptyList())
                .build();
    }

    // OAuth2User의 attributes를 만들기 위한 메소드 추가
    public Map<String, Object> getAttributes() {
        return Map.of(
                "id", oauthId,
                "name", name,
                "email", email
        );
    }
}