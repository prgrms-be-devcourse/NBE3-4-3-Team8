package com.ll.nbe342team8.domain.oauth.controller;

import com.ll.nbe342team8.domain.jwt.JwtService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null) {
            throw new ServiceException(401, "리프레시 토큰이 없습니다.");
        }

        // 리프레시 토큰 검증
        if (!jwtService.validateToken(refreshToken)) {
            throw new ServiceException(401, "유효하지 않은 리프레시 토큰입니다.");
        }

        try {
            String kakaoId = jwtService.getKakaoIdFromToken(refreshToken);
            Member member = memberRepository.findByoAuthId(kakaoId)
                    .orElseThrow(() -> new ServiceException(404, "사용자를 찾을 수 없습니다."));

            // 새로운 액세스 토큰 생성
            String newAccessToken = jwtService.generateToken(member);

            // 쿠키 설정
            ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                    .path("/")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")  // 개발 환경에 맞춤
                    .maxAge(60 * 60)  // 1시간
                    .build();

            return ResponseEntity.ok()
                    .header("Set-Cookie", accessTokenCookie.toString())
                    .body("액세스 토큰이 갱신되었습니다.");
        } catch (Exception e) {
            throw new ServiceException(401, "토큰 갱신에 실패했습니다.");
        }
    }
}
