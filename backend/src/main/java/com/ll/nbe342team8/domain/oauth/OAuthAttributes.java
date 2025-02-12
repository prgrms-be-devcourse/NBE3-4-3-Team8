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
    private String oAuthId;  // kakaoId를 oAuthId로 변경
    private String name;
    private String email;   //
    private String profileImageUrl;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String oAuthId,
                           String name,
                           String email,
                           String profileImageUrl) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.oAuthId = oAuthId;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        // kakao_account에서 필요한 정보 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .profileImageUrl((String) profile.get("profile_image_url"))
                .email((String) kakaoAccount.get("email"))
                .oAuthId(String.valueOf(attributes.get("id")))  //
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .oAuthId(oAuthId)
                .name(name)
                .email(email)
                .phoneNumber("")
                .memberType(Member.MemberType.USER)
                .deliveryInformations(Collections.emptyList())
                .profileImageUrl(profileImageUrl)
                .build();
    }

    // OAuth2User의 attributes를 만들기 위한 메소드 추가
    public Map<String, Object> getAttributes() {
        return Map.of(
                "id", oAuthId,
                "name", name,
                "email", email,
                "profileImageUrl", profileImageUrl
        );
    }
}