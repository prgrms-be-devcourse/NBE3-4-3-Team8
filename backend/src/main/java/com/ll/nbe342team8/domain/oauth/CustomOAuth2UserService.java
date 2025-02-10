package com.ll.nbe342team8.domain.oauth;


import com.ll.nbe342team8.domain.jwt.JwtService;
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final JwtService jwtService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(request);

        // 안전한 타입 캐스팅과 널 체크
        Map<String, Object> attributes = oauth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.getOrDefault("kakao_account", new HashMap<>());
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.getOrDefault("profile", new HashMap<>());

        String oauthId = oauth2User.getName();  // kakaoId -> oauthId
        String email = (String) kakaoAccount.getOrDefault("email", "");
        String name = (String) profile.getOrDefault("nickname", "");  // nickname -> name


        PutReqMemberMyPageDto dto = new PutReqMemberMyPageDto();
        dto.setName(name);  // 닉네임 설정
        dto.setPhoneNumber(""); // 기본 전화번호 설정 (빈 값)

        Member member = memberService.modifyOrJoin(oauthId, dto, email);
        String refreshToken = jwtService.generateRefreshToken(member);

        return new SecurityUser(member);
    }
}