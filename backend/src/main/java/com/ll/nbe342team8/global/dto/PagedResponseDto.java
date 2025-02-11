package com.ll.nbe342team8.global.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public record PagedResponseDto<T>(
		List<T> content,
		int page,
		int pageSize,
		long totalElements,
		int totalPages,
		boolean isLast
) {
	public static <T> PagedResponseDto<T> from(Page<T> pageData) {
		return new PagedResponseDto<>(
				pageData.getContent(),
				pageData.getNumber(),
				pageData.getSize(),
				pageData.getTotalElements(),
				pageData.getTotalPages(),
				pageData.isLast()
		);
	}
}
