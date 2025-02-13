package com.ll.nbe342team8.domain.jwt;


import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            log.info("현재 요청 URI: {}", request.getRequestURI());
            String token = extractTokenFromCookies(request);
            log.info("쿠키에서 추출된 토큰 존재 여부: {}", token != null);

            if (token != null && jwtService.validateToken(token)) {
                String kakaoId = jwtService.getKakaoIdFromToken(token); // findByOauthId랑 기능 비교하기
                log.info("토큰에서 추출된 카카오 ID: {}", kakaoId);

                Member member = memberService.findByOauthId(kakaoId)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                log.info("회원 조회 성공: {}", member.getEmail());

                SecurityUser securityUser = new SecurityUser(member);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        securityUser, null, securityUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("SecurityContextHolder 최종 인증 정보: {}", SecurityContextHolder.getContext().getAuthentication());
                log.info("인증 정보 설정 완료");
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생: ", e);
        }

        chain.doFilter(request, response);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        log.info("요청에 포함된 쿠키 개수: {}", cookies != null ? cookies.length : 0);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("쿠키 정보 - 이름: {}, 값 존재 여부: {}",
                        cookie.getName(), cookie.getValue() != null);
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/books") || path.equals("/login") || path.equals("/cart/anonymous");
    }
}