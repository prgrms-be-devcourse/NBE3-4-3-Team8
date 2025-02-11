package com.ll.nbe342team8.domain.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.nbe342team8.domain.admin.exception.AdminException;
import com.ll.nbe342team8.global.exceptions.ErrorCode;

@RestController
@RequestMapping("/admin")
public class AdminDashboardController {

	@GetMapping("/dashboard")
	public ResponseEntity<?> getAdminDashboard(@AuthenticationPrincipal User adminUser) {

		if (adminUser == null) {
			throw new AdminException(ErrorCode.UNAUTHORIZED_ACCESS); // 예외 발생
		}

		return ResponseEntity.ok().body(
				"관리자 대시보드 접근 성공! 로그인한 관리자: " + adminUser.getUsername()
		);
	}
}
