package com.ll.nbe342team8.domain.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${custom.site.frontUrl}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 프론트엔드로 리다이렉트할 URL을 생성
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("login", "success")
                .build().toUriString();

        // 세션 유지를 위해 쿠키의 SameSite 속성을 설정
        response.setHeader("Set-Cookie", "JSESSIONID=" + request.getSession().getId()
                + "; Path=/; HttpOnly; SameSite=Lax");

        // 프론트엔드로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}