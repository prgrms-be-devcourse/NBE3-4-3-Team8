package com.ll.nbe342team8.global.baseInit.data;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ll.nbe342team8.domain.admin.repository.AdminLoginRepository;
import com.ll.nbe342team8.domain.member.member.entity.Member;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer {
	private final AdminLoginRepository adminLoginRepository;
	private final PasswordEncoder passwordEncoder;

	// env 파일 로드
	private final Dotenv dotenv = Dotenv.load();

	@PostConstruct
	public void init() {
		String adminEmail = dotenv.get("ADMIN_EMAIL");
		String adminPassword = dotenv.get("ADMIN_PASSWORD");

		// 읽은 값 콘솔 출력 (디버깅 용도)
		System.out.println("ADMIN_EMAIL: " + adminEmail);
		System.out.println("ADMIN_PASSWORD: " + adminPassword);

		if (adminLoginRepository.countByMemberType(Member.MemberType.ADMIN) == 0) {
			Member admin = Member.builder()
					.name("Admin")
					.email(adminEmail)
					.password(passwordEncoder.encode(adminPassword))
					.memberType(Member.MemberType.ADMIN)
					.build();
			adminLoginRepository.save(admin);
			System.out.println("기본 관리자 계정 생성 완료");
		}
	}
}
