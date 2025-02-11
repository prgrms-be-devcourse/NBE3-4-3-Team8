package com.ll.nbe342team8.domain.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest { // 로그인 요청 DTO
	private String email;
	private String password;
}
