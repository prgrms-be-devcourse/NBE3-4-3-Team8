package com.ll.nbe342team8.auth;

import com.ll.nbe342team8.domain.jwt.JwtService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("Test")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    private String refreshToken;

    @BeforeEach
    void setup() {
        testMember = Member.builder()
                .name("Test1")
                .email("example@123.com")
                .oAuthId("test123")
                .memberType(Member.MemberType.USER)
                .phoneNumber("010-1234-5678")
                .deliveryInformations(new ArrayList<>())
                .build();
        memberRepository.save(testMember);

        refreshToken = jwtService.generateRefreshToken(testMember);
    }

    @Test
    @DisplayName("리프레쉬 토큰 - 액세스 토큰 재발급")
    void refreshAccessToken() throws Exception {

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

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
}