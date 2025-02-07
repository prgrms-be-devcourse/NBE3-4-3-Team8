package com.ll.nbe342team8.domain.jwt;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. 토큰 추출
        String token = extractTokenFromCookie(request);

        // 2. 토큰 없는 경우 즉시 통과 (shouldNotFilter와 중복 방지)
        if (token == null || token.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // 3. 토큰 유효성 검증 우선 수행
            if (!jwtService.validateToken(token) || !jwtService.isTokenValid(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }

            // 4. 인증 정보 추출
            String oAuthId = jwtService.getKakaoIdFromToken(token);
            Optional<Member> opMember = memberService.findByOauthId(oAuthId);

            if (opMember.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Member not found");
                return;
            }

            // 5. 인증 객체 설정
            Member member = opMember.get();
            SecurityUser securityUser = new SecurityUser(member);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // 6. 요청 속성에 Member 저장 (선택적)
            request.setAttribute("member", member);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed: " + e.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/books") || path.equals("/login");
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

