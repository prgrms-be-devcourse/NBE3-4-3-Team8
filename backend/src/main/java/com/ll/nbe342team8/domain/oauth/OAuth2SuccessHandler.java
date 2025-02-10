package com.ll.nbe342team8.domain.oauth;


import com.ll.nbe342team8.domain.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        log.info("OAuth 인증 성공: 사용자 이메일 = {}", securityUser.getMember().getEmail());

        String jwtToken = jwtService.generateToken(securityUser.getMember());
        String refreshToken = jwtService.generateRefreshToken(securityUser.getMember());


        log.info("JWT 토큰 생성 완료: accessToken 존재 = {}, refreshToken 존재 = {}",
                jwtToken != null, refreshToken != null);

        // 쿠키 설정
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", jwtToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")  // 크로스 사이트 요청에 대한 제한 완화
                .maxAge(60 * 60)  // 1시간
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(7 * 24 * 60 * 60)  // 7일
                .build();
        log.info("쿠키 설정: accessToken = {}, refreshToken = {}",
                accessTokenCookie.toString(), refreshTokenCookie.toString());

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/");
    }
}