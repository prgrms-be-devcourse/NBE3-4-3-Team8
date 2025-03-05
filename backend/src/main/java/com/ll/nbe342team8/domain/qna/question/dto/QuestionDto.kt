package com.ll.nbe342team8.domain.qna.question.dto

import com.ll.nbe342team8.domain.qna.answer.dto.AnswerDto
import com.ll.nbe342team8.domain.qna.question.entity.Question
import java.time.LocalDateTime

data class QuestionDto(
    val id:  Long,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val title: String,
    val content: String,
    val isAnswer: Boolean,
    val answers: List<AnswerDto>,
    val genFiles: List<QuestionGenFileDto>
) {
    constructor(question: Question) : this(
        id = question.id,
        createDate = question.createDate,
        modifyDate = question.modifyDate,
        title = question.title,
        content = question.content,
        isAnswer = question.answers?.isNotEmpty() ?: false,
        answers = question.answers?.map { AnswerDto(it) } ?: emptyList(),
        genFiles = question.genFiles?.map { QuestionGenFileDto(it) } ?: emptyList()
    )
}