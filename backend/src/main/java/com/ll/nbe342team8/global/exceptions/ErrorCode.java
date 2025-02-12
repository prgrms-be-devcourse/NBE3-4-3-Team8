package com.ll.nbe342team8.global.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	// ✅ 공통 에러 코드
	INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),

	// ✅ 관리자 관련 에러 코드
	ADMIN_NOT_FOUND("관리자 계정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	INVALID_PASSWORD("비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
	LOGIN_ATTEMPT_EXCEEDED("5회 이상 로그인 실패. 30분 후 다시 시도하세요.", HttpStatus.FORBIDDEN),
	ADMIN_ACCESS_DENIED("관리자 권한이 없습니다.", HttpStatus.FORBIDDEN),
	UNAUTHORIZED_ACCESS("관리자 인증 실패: 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
	ADMIN_CREATION_FAILED("관리자 계정을 생성할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

	private final String message;
	private final HttpStatus status;

	ErrorCode(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}
}
