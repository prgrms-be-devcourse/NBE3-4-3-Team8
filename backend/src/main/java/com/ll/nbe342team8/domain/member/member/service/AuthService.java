package com.ll.nbe342team8.domain.member.member.service;

import com.ll.nbe342team8.domain.member.member.dto.KakaoTokenResponse;
import com.ll.nbe342team8.domain.member.member.dto.KakaoUserResponse;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.standard.util.Ut;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoTokenResponse getKakaoToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        // 1️⃣ HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept-Charset", "UTF-8"); // 인코딩 설정 추가 (선택 사항)

        // 2️⃣ 요청 바디 설정 (폼 데이터)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id","1989bba61411e1765a90612b4d9de8a3"); // 여기에 카카오 REST API 키 입력
        body.add("redirect_uri","http://localhost:8080/auth/login/kakao"); // 여기에 리다이렉트 URI 입력
        body.add("code", code); // 클라이언트에서 받은 인가 코드

        // 3️⃣ HttpEntity 생성 (헤더 + 바디)
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 4️⃣ POST 요청 전송
        ResponseEntity<KakaoTokenResponse> responseEntity =
                restTemplate.exchange(url, HttpMethod.POST, requestEntity, KakaoTokenResponse.class);

        // 5️⃣ 응답 본문 반환
        return responseEntity.getBody();
    }

    public KakaoUserResponse requestProfile(KakaoTokenResponse oAuthToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        // 1️⃣ 요청 헤더 설정 (Bearer 토큰 추가)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + oAuthToken.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2️⃣ HttpEntity 생성 (헤더 추가)
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // 3️⃣ GET 요청 보내고 응답을 DTO로 변환
        ResponseEntity<KakaoUserResponse> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, requestEntity, KakaoUserResponse.class);

        // 4️⃣ DTO 객체 반환
        return responseEntity.getBody();
    }



    public Member createNewUser(KakaoUserResponse kakaoUserResponse) {
        String email = kakaoUserResponse.getKakaoAccount().getEmail();
        Member member=new Member();
        if(memberRepository.findByEmail(email).isEmpty()) {
            member=Member.builder()
                    .email(email)
                    .name("nodonghui")
                    .phoneNumber("010-1234-5678")
                    .build();
            memberRepository.save(member);
        }
        return member;
    }
}
