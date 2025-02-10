package com.ll.nbe342team8.domain.admin.exception;

import org.springframework.http.HttpStatus;

import com.ll.nbe342team8.global.exceptions.ErrorCode;

import lombok.Getter;

@Getter
public class AdminException extends RuntimeException {
	private final HttpStatus status;

	public AdminException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.status = errorCode.getStatus();
	}
}
