package com.ll.nbe342team8.domain.qna.answer.dto

import com.ll.nbe342team8.domain.qna.answer.entity.Answer
import java.time.LocalDateTime

data class AnswerDto(
    val id: Long,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val content: String
) {
    constructor(answer: Answer) : this(
        id = answer.id,
        createDate = answer.createDate,
        modifyDate = answer.modifyDate,
        content = answer.content
    )
}
