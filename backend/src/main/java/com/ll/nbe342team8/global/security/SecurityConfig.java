package com.ll.nbe342team8.global.security;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ll.nbe342team8.domain.jwt.JwtAuthenticationFilter;
import com.ll.nbe342team8.domain.jwt.JwtService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.CustomOAuth2UserService;
import com.ll.nbe342team8.domain.oauth.OAuth2SuccessHandler;
import com.ll.nbe342team8.domain.oauth.SecurityUser;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 사용
public class SecurityConfig {
	private final JwtService jwtService;
	private final MemberService memberService;
	private final OAuth2AuthorizedClientService authorizedClientService;
	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// 관리자 로그인 페이지는 모든 사용자에게 허용
						.requestMatchers("/admin/login").permitAll()
						// 그 외 관리자 페이지는 관리자 권한이 있는 사용자에게만 허용
						.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
						.requestMatchers("/api/public/**", "/oauth2/**", "/api/auth/**", "/refresh", "/api/auth/refresh", "/swagger-ui/**", "/v3/api-docs/**", "/api/auth/me", "/api/auth/me/**").permitAll()
						.requestMatchers("/my/orders").permitAll()
						.requestMatchers("/books/**", "/event/**", "/images/**", "/cart/**").permitAll() // 카트, 메인페이지 추가
						.requestMatchers(HttpMethod.GET, "/reviews/**", "/cart").permitAll()
						.anyRequest().authenticated()
				)
				.addFilterBefore(new JwtAuthenticationFilter(jwtService, memberService),
						UsernamePasswordAuthenticationFilter.class)
				.headers((headers) -> headers
						.addHeaderWriter(new XFrameOptionsHeaderWriter(
								XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
				.oauth2Login(oauth2 -> oauth2
						.authorizationEndpoint(authorization -> authorization
								.baseUri("/oauth2/authorization")
								.authorizationRequestRepository(new HttpSessionOAuth2AuthorizationRequestRepository())
						)
						.redirectionEndpoint(redirection -> redirection
								.baseUri("/login/oauth2/code/*")
						)
						.successHandler(oAuth2SuccessHandler())
						.userInfoEndpoint(userInfo -> userInfo
								.userService(customOAuth2UserService)
						)
				)
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint((request, response, authException) -> {
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.setContentType("application/json;charset=UTF-8");
							response.getWriter().write("인증이 필요합니다.");
						})
				);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(List.of("http://localhost:3000"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setExposedHeaders(Arrays.asList("Authorization", "RefreshToken"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public OAuth2SuccessHandler oAuth2SuccessHandler() {
		return new OAuth2SuccessHandler(jwtService, authorizedClientService);
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, MemberService memberService) throws Exception {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(identifier -> {
			// 이메일을 입력하면, oAuthId로 변환하여 인증
			Optional<Member> optionalMember;
			if (identifier.contains("@")) {
				optionalMember = memberService.findByEmail(identifier);
			} else {
				optionalMember = memberService.findByOauthId(identifier);
			}

			if (optionalMember.isEmpty()) {
				throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
			}

			return new SecurityUser(optionalMember.get());
		});

		authenticationProvider.setPasswordEncoder(passwordEncoder());

		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // BCrypt 알고리즘 사용
	}
}
