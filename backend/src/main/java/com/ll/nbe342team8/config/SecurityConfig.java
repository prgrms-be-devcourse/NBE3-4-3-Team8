package com.ll.nbe342team8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authz -> authz
                        .requestMatchers("/my/orders").permitAll()  // 주문 조회 API는 인증 없이 접근 가능
                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )
                .csrf().disable();  // CSRF 비활성화

        return http.build();
    }
}