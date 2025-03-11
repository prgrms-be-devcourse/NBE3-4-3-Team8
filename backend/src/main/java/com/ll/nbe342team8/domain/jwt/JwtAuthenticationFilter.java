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
import java.util.Optional;

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
            Optional<String> tokenOpt = extractTokenFromCookies(request);

            if (tokenOpt.isPresent()) {
                String token = tokenOpt.get();
                authenticateWithToken(token);
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생: ", e);
        }

        chain.doFilter(request, response);
    }

    /**
     * 토큰을 사용하여 사용자 인증을 처리합니다.
     *
     * @param token JWT 토큰
     */
    private void authenticateWithToken(String token) {
        if (jwtService.validateToken(token)) {
            String oAuthId = jwtService.getKakaoIdFromToken(token);
            log.info("토큰에서 추출된 oAuthId: {}", oAuthId);

            try {
                Member member = memberService.findByOauthId(oAuthId)
                        .orElseThrow(() -> new UsernameNotFoundException("ID: " + oAuthId + "에 해당하는 사용자를 찾을 수 없습니다."));

                log.info("회원 인증 성공: {}", member.getEmail());

                SecurityUser securityUser = new SecurityUser(member);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        securityUser, null, securityUser.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("SecurityContext에 인증 정보 설정 완료");
            } catch (UsernameNotFoundException e) {
                log.error("사용자 조회 실패: {}", e.getMessage());
            }
        } else {
            log.warn("유효하지 않은 토큰입니다.");
        }
    }

    /**
     * 요청의 쿠키에서 accessToken을 추출합니다.
     */
    private Optional<String> extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            log.debug("요청에 쿠키가 없습니다.");
            return Optional.empty();
        }

        log.debug("요청에 포함된 쿠키 개수: {}", cookies.length);

        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName()) && cookie.getValue() != null) {
                log.debug("accessToken 쿠키를 찾았습니다.");
                return Optional.of(cookie.getValue());
            }
        }

        log.debug("accessToken 쿠키를 찾을 수 없습니다.");
        return Optional.empty();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // SecurityConfig의 permitAll() 경로와 일치시킴
        return path.startsWith("/api/public/") ||
                path.startsWith("/oauth2/") ||
                path.equals("/refresh") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs/") ||
                path.equals("/my/orders") ||
                path.startsWith("/books/") ||
                path.startsWith("/event/") ||
                path.startsWith("/images/") ||
                (request.getMethod().equals("GET") && (path.startsWith("/reviews/") || path.equals("/cart"))) ||
                path.equals("/admin/login");
    }
}