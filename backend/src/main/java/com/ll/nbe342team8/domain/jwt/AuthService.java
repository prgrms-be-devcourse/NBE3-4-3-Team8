package com.ll.nbe342team8.domain.jwt;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    // ✅ 토큰 유효성 검사 - Authorization 헤더 제거, 쿠키 사용
    public Member validateTokenAndGetMember(@CookieValue(value = "accessToken", required = false) String token) {
        if (token == null) {
            throw new ServiceException(401, "로그인이 필요합니다.");
        }

        if (!jwtService.validateToken(token)) {
            throw new ServiceException(401, "Invalid token");
        }

        String kakaoId = jwtService.getKakaoIdFromToken(token);
        return memberRepository.findByoAuthId(kakaoId)
                .orElseThrow(() -> new ServiceException(404, "사용자를 찾을 수 없습니다."));
    }

    // ✅ JWT 기반 로그인 처리 - 쿠키로 `accessToken`, `refreshToken` 설정
    public ResponseEntity<?> authenticate(@CookieValue(value = "accessToken", required = false) String token) {
        Member member = validateTokenAndGetMember(token);

        String newAccessToken = jwtService.generateToken(member);
        String newRefreshToken = jwtService.generateRefreshToken(member);

        // ✅ 쿠키에 `accessToken`, `refreshToken` 저장
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("None")
                .path("/")
                .maxAge(60 * 60) // 1시간
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("None")
                .path("/api/auth/refresh")
                .maxAge(7 * 24 * 60 * 60) // 7일
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", accessTokenCookie.toString())
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body("Authentication successful");
    }

    // ✅ 사용자 정보 조회 - 쿠키 기반 인증 적용
    public Member getMemberFromToken(@CookieValue(value = "accessToken", required = false) String token) {
        if (token == null) {
            throw new ServiceException(401, "로그인이 필요합니다.");
        }

        if (!jwtService.validateToken(token)) {
            throw new ServiceException(401, "Invalid token");
        }

        String kakaoId = jwtService.getKakaoIdFromToken(token);
        return memberRepository.findByoAuthId(kakaoId)
                .orElseThrow(() -> new ServiceException(404, "사용자를 찾을 수 없습니다."));

    }

    // ✅ 로그아웃 - `Set-Cookie`로 `accessToken`, `refreshToken` 삭제
    public ResponseEntity<?> logout() {
        ResponseCookie deleteAccessToken = ResponseCookie.from("accessToken", "")
                .path("/")
                .maxAge(0)
                .secure(true)
                .build();

        ResponseCookie deleteRefreshToken = ResponseCookie.from("refreshToken", "")
                .path("/")
                .maxAge(0)
                .secure(true)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", deleteAccessToken.toString())
                .header("Set-Cookie", deleteRefreshToken.toString())
                .body("로그아웃 되었습니다.");
    }
}