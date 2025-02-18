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
import java.util.Optional;

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

        String oAuthId = oauth2User.getName();  // kakaoId -> oAuthId
        String email = (String) kakaoAccount.getOrDefault("email", "");
        String name = (String) profile.getOrDefault("nickname", "");  // nickname -> name
        String profileImageUrl = (String) profile.getOrDefault("profile_image_url", "");

        Optional<Member> existingMember = memberService.findByOauthId(oAuthId);
        String phoneNumber = (existingMember.isPresent()) ? existingMember.get().getPhoneNumber() : ""; // 기존 유저면 phoneNumber 유지, 없으면 빈 값

        PutReqMemberMyPageDto dto = new PutReqMemberMyPageDto(name,phoneNumber);

        Member member = memberService.modifyOrJoin(oAuthId, dto, email);
        String refreshToken = jwtService.generateRefreshToken(member);  // generateRefreshToken에서 리프레시 토큰 값 설정하는건지 확인

        return new SecurityUser(member);
    }
}