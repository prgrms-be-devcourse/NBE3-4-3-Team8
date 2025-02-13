package com.ll.nbe342team8.global.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	private String message;
	private int status;
}
