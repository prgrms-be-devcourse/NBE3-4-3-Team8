package com.ll.nbe342team8.domain.member.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoProfile {

    private String nickname; // 닉네임

    @JsonProperty("thumbnail_image_url")
    private String thumbnailImageUrl; // 썸네일 이미지 URL

    @JsonProperty("profile_image_url")
    private String profileImageUrl; // 프로필 이미지 URL

    @JsonProperty("is_default_image")
    private boolean isDefaultImage; // 기본 이미지 여부
}
