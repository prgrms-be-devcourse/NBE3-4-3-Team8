package com.ll.nbe342team8.auth;

import com.ll.nbe342team8.domain.jwt.JwtService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JwtAuthentication {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;
    private Member adminMember;
    private String accessToken;
    private String refreshToken;
    private String adminAccessToken;


    @BeforeEach
    void setup() {
        // 테스트용 일반 사용자 생성
        testMember = Member.builder()
                .name("테스트 사용자")
                .email("testuser@example.com")
                .oAuthId("test-oauth-id-123")
                .memberType(Member.MemberType.USER)
                .phoneNumber("010-1234-5678")
                .deliveryInformations(new ArrayList<>())
                .build();
        memberRepository.save(testMember);

        // 테스트용 관리자 사용자 생성
        adminMember = Member.builder()
                .name("관리자")
                .email("admin@example.com")
                .oAuthId("admin-oauth-id-456")
                .memberType(Member.MemberType.ADMIN)
                .phoneNumber("010-8765-4321")
                .deliveryInformations(new ArrayList<>())
                .build();
        memberRepository.save(adminMember);

        // 토큰 생성
        accessToken = jwtService.generateToken(testMember);
        refreshToken = jwtService.generateRefreshToken(testMember);
        adminAccessToken = jwtService.generateToken(adminMember);
    }

    @Test
    @DisplayName("액세스 토큰 검증 테스트")
    void validateAccessToken() {
        // 액세스 토큰 검증
        boolean isValid = jwtService.validateToken(accessToken);
        assertTrue(isValid, "유효한 액세스 토큰은 검증을 통과해야 합니다");

        // 액세스 토큰에서 정보 추출 테스트
        String extractedOAuthId = jwtService.getKakaoIdFromToken(accessToken);
        assertEquals(testMember.getOAuthId(), extractedOAuthId, "토큰에서 추출한 OAuthId가 일치해야 합니다");
    }

    @Test
    @DisplayName("리프레시 토큰 검증 테스트")
    void validateRefreshToken() {
        // 리프레시 토큰 검증
        boolean isValid = jwtService.validateToken(refreshToken);
        assertTrue(isValid, "유효한 리프레시 토큰은 검증을 통과해야 합니다");

        // 리프레시 토큰에서 정보 추출 테스트
        String extractedOAuthId = jwtService.getKakaoIdFromToken(refreshToken);
        assertEquals(testMember.getOAuthId(), extractedOAuthId, "토큰에서 추출한 OAuthId가 일치해야 합니다");
    }

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰 재발급 API 테스트")
    void refreshAccessTokenApi() throws Exception {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");

        ResultActions resultActions = mockMvc.perform(
                        post("/api/auth/refresh")
                                .cookie(refreshTokenCookie)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(header().string("Set-Cookie", containsString("accessToken")))
                .andExpect(content().string(containsString("액세스 토큰이 갱신되었습니다")));
    }

    @Test
    @DisplayName("액세스 토큰으로 보호된 API 접근 테스트")
    void accessProtectedApiWithAccessToken() throws Exception {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");

        // 보호된 내 정보 API 호출
        ResultActions resultActions = mockMvc.perform(
                        get("/api/auth/me")
                                .cookie(accessTokenCookie)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testMember.getName()))
                .andExpect(jsonPath("$.email").value(testMember.getEmail()));
    }

    @Test
    @DisplayName("만료된 액세스 토큰으로 API 접근 실패 시뮬레이션")
    void accessApiWithExpiredToken() throws Exception {
        // 만료된 토큰 시뮬레이션
        String expiredToken = "expired_token_simulation";
        Cookie expiredTokenCookie = new Cookie("accessToken", expiredToken);
        expiredTokenCookie.setPath("/");

        ResultActions resultActions = mockMvc.perform(
                        get("/api/auth/me")
                                .cookie(expiredTokenCookie)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("관리자 권한 확인 테스트")
    void verifyAdminRole() throws Exception {
        Cookie adminTokenCookie = new Cookie("accessToken", adminAccessToken);
        adminTokenCookie.setPath("/");

        // SecurityContext에 인증 정보 설정
        SecurityUser securityUser = new SecurityUser(adminMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                securityUser,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 관리자 권한 필요한 API 호출 (예: 관리자 대시보드)
        ResultActions resultActions = mockMvc.perform(
                        get("/admin/dashboard/questions")
                                .cookie(adminTokenCookie)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logoutTest() throws Exception {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");

        ResultActions resultActions = mockMvc.perform(
                        post("/api/auth/me/logout")
                                .cookie(accessTokenCookie)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(header().string("Set-Cookie", containsString("accessToken=;")))
                .andExpect(content().string(containsString("로그아웃 되었습니다")));

        // 로그아웃 후 보호된 API 접근 시도 (쿠키가 삭제되어 인증 실패)
        ResultActions postLogoutActions = mockMvc.perform(
                        get("/api/auth/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        postLogoutActions.andExpect(status().isUnauthorized());
    }
}
