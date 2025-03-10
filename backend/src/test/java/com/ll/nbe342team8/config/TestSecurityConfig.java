package com.ll.nbe342team8.config;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.entity.Member.MemberType;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class TestSecurityConfig {

    @Bean
    public SecurityUser testSecurityUser() {
        // 테스트용 Member 객체 생성 (여기서는 ADMIN 권한이 아닌 USER 권한으로 생성)
        Member member = Member.builder()
                .id(1L)
                .oAuthId("testuser")
                .email("testuser@example.com")
                .name("Test User")
                .memberType(MemberType.USER)
                .profileImageUrl("http://example.com/profile.png")
                .carts(new ArrayList<>())
                .build();
        // SecurityUser는 Member를 래핑하는 커스텀 UserDetails 구현체라고 가정합니다.
        return new SecurityUser(member);
    }
}
