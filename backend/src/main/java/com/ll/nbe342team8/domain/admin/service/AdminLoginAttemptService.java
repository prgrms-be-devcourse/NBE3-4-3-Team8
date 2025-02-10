package com.ll.nbe342team8.domain.admin.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ll.nbe342team8.domain.admin.exception.AdminException;
import com.ll.nbe342team8.global.exceptions.ErrorCode;

@Service
public class AdminLoginAttemptService {
	private final Map<String, Integer> loginAttempts = new HashMap<>(); // 로그인 시도 횟수
	private static final int MAX_ATTEMPTS = 5; // 최대 시도 횟수(5회)

	// 로그인 시도 횟수 증가 (실패 시)
	public void recordFailedLogin(String email) {
		loginAttempts.put(email, loginAttempts.getOrDefault(email, 0) + 1);
	}

	// 로그인 시도 횟수 확인 (시도 시, 5회 이상 시 예외 발생)
	public void checkLoginAttempts(String email) {
		if (loginAttempts.getOrDefault(email, 0) >= MAX_ATTEMPTS) {
			throw new AdminException(ErrorCode.LOGIN_ATTEMPT_EXCEEDED);
		}
	}

	// 로그인 시도 횟수 초기화 (성공 시)
	public void resetLoginAttempts(String email) {
		loginAttempts.remove(email);
	}
}
