package com.ll.nbe342team8.global.baseInit.data;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() {
		String adminEmail = "admin@thebook.co.kr";
		String adminPassword = "1111";

		if (memberRepository.countByMemberType(Member.MemberType.ADMIN) == 0) {
			String generatedOAuthId = "admin-" + UUID.randomUUID(); // 랜덤 oAuthId 생성

			Member admin = Member.builder()
					.name("Admin")
					.email(adminEmail)
					.password(passwordEncoder.encode(adminPassword))
					.memberType(Member.MemberType.ADMIN)
					.oAuthId(generatedOAuthId) // oAuthId 자동 생성
					.build();
			memberRepository.save(admin);
			System.out.println("기본 관리자 이메일: " + adminEmail);
			System.out.println("기본 관리자 비밀번호: " + adminPassword);
			System.out.println("기본 관리자 oAuthId 할당: " + generatedOAuthId);
			System.out.println("기본 관리자 계정 생성 완료");

		}
	}
}
