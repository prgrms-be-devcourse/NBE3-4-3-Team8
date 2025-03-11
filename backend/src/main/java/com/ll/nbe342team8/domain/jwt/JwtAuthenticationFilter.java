package com.ll.nbe342team8.domain.jwt;


import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            log.info("현재 요청 URI: {}", request.getRequestURI());
            String token = extractTokenFromCookies(request);
            log.info("쿠키에서 추출된 토큰 존재 여부: {}", token != null);

            if (token != null && jwtService.validateToken(token)) {
                // 토큰에서 Claims 추출
                Claims claims = jwtService.extractAllClaims(token);

                // Claims에서 필요한 정보 추출
                String oAuthId = claims.getSubject();
                Long id = claims.get("id", Long.class);
                String email = claims.get("email", String.class);
                String name = claims.get("name", String.class);
                String memberTypeStr = claims.get("memberType", String.class);
                Member.MemberType memberType = Member.MemberType.valueOf(memberTypeStr);

                // authorities 정보 추출 및 변환
                @SuppressWarnings("unchecked")
                List<String> authorities = claims.get("authorities", List.class);
                List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                log.info("토큰에서 추출된 사용자 정보 - ID: {}, 이메일: {}, 이름: {}, 타입: {}",
                        id, email, name, memberType);

                // DB 조회 없이 Member 객체 생성
                Member member = Member.builder()
                        .id(id)
                        .oAuthId(oAuthId)
                        .email(email)
                        .name(name)
                        .memberType(memberType)
                        .build();

                // SecurityUser 및 인증 객체 생성
                SecurityUser securityUser = new SecurityUser(member);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        securityUser, null, grantedAuthorities);

                // SecurityContext에 인증 객체 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("JWT 기반 인증 완료: {}", securityUser.getUsername());
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
                log.info("쿠키 정보 - 이름: {}, 값: {}", cookie.getName(), cookie.getValue());
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

        // SecurityConfig의 permitAll() 경로와 일치시킴
        return path.startsWith("/api/public") ||
                path.startsWith("/oauth2") ||
                path.equals("/refresh") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/books") ||
                path.startsWith("/event") ||
                path.startsWith("/images") ||
                path.startsWith("/api/payments") ||
                path.startsWith("/order") ||
                (request.getMethod().equals("GET") && (path.startsWith("/reviews"))) ||
                path.equals("/admin/login");
    }
}