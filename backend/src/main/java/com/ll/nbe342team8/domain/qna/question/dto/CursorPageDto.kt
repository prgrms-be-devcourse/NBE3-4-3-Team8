package com.ll.nbe342team8.domain.qna.question.dto

import java.time.Instant
import java.time.LocalDateTime

data class CursorPageDto<T>(
    val items: List<T>, // 데이터 목록
    val nextCursor: LocalDateTime?, // 다음 페이지를 위한 커서 값
    val prevCursor: LocalDateTime? // 이전 페이지를 위한 커서 값
)