package com.ll.nbe342team8.domain.oauth;

import com.ll.nbe342team8.domain.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
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
    private final JwtService jwtService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        SecurityUser securityUser = (SecurityUser) oAuth2User;

        String jwtToken = jwtService.generateToken(securityUser.getMember());

        String targetUrl = UriComponentsBuilder
                .fromUriString(redirectUri)
                .queryParam("token", jwtToken)
                .build()
                .toUriString();

        // 프론트엔드로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}