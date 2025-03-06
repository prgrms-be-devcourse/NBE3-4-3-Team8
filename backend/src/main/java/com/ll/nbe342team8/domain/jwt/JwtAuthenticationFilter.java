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

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MemberService memberService;

    public JwtAuthenticationFilter(JwtService jwtService, MemberService memberService) {
        this.jwtService = jwtService;
        this.memberService = memberService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        log.info("[JwtAuthenticationFilter] === doFilterInternal 시작 ===");
        log.info("[JwtAuthenticationFilter] 요청 URI: {}", request.getRequestURI());

        try {
            String token = extractTokenFromCookies(request);
            log.info("[JwtAuthenticationFilter] 쿠키에서 추출된 토큰: {}", token);

            if (token != null && jwtService.validateToken(token)) {
                String kakaoId = jwtService.getKakaoIdFromToken(token);
                log.info("[JwtAuthenticationFilter] 토큰에서 추출된 kakaoId: {}", kakaoId);

                Member member = memberService.findByOauthId(kakaoId)
                        .orElse(null);

                if (member == null) {
                    log.warn("[JwtAuthenticationFilter] 해당 kakaoId를 가진 멤버가 없습니다.");
                } else {
                    log.info("[JwtAuthenticationFilter] 멤버 조회 성공: {} (ID: {})", member.getEmail(), member.getId());

                    SecurityUser securityUser = new SecurityUser(member);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            securityUser, null, securityUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("[JwtAuthenticationFilter] SecurityContextHolder 인증 정보 설정 완료");
                }
            } else {
                log.info("[JwtAuthenticationFilter] 토큰이 없거나, 검증에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("[JwtAuthenticationFilter] JWT 인증 처리 중 예외 발생: ", e);
        }

        log.info("[JwtAuthenticationFilter] === doFilterInternal 끝 ===");
        chain.doFilter(request, response);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.info("[JwtAuthenticationFilter] 요청에 쿠키가 없습니다.");
            return null;
        }

        for (Cookie cookie : cookies) {
            log.info("[JwtAuthenticationFilter] 쿠키 이름: {}, 쿠키 값: {}", cookie.getName(), cookie.getValue());
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 특정 경로는 필터를 타지 않도록 설정할 수 있는 메서드.
     * 현재 "/my/orders/create"가 여기서 제외되어 있다면, 해당 경로로의 요청은 토큰 검증을 안 거칩니다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // 로그 추가
        log.info("[JwtAuthenticationFilter] shouldNotFilter path: {}", path);

        // 여기가 true를 리턴하면, 해당 경로는 이 필터를 거치지 않게 됩니다.
        // 예) /my/orders/create 를 제외시키면 토큰 검증 안 거침.
        if (path.startsWith("/my/orders/create")) {
            log.info("[JwtAuthenticationFilter] /my/orders/create 요청 -> 필터를 타지 않음(shouldNotFilter=true)");
            return true;
        }

        // 나머지 로직
        return path.startsWith("/books")
                || path.equals("/login")
                || path.equals("/cart/anonymous")
                || path.startsWith("/api/payments")
                || path.startsWith("/order");
    }
}
