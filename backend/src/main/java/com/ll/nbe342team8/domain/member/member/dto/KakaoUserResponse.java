package com.ll.nbe342team8.domain.member.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserResponse {

    private Long id; // 카카오 회원 번호

    @JsonProperty("connected_at")
    private String connectedAt; // 카카오 서비스 연결 시간

    private KakaoUserProperties properties; // 사용자 프로필 정보
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount; // 카카오 계정 정보
}
