package com.ll.nbe342team8.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse { // 로그인 응답 DTO
	private String token; // JWT 토큰
}
