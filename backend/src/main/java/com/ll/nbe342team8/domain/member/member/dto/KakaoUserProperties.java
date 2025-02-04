package com.ll.nbe342team8.domain.member.member.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserProperties {

    private String nickname; // 닉네임

    @JsonProperty("profile_image")
    private String profileImage; // 프로필 이미지 URL

    @JsonProperty("thumbnail_image")
    private String thumbnailImage; // 썸네일 이미지 URL
}
