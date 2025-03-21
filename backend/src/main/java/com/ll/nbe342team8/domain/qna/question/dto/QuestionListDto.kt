package com.ll.nbe342team8.domain.qna.question.dto

import com.ll.nbe342team8.domain.qna.question.entity.Question
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

//질문 목록 조회시 사용
data class QuestionListDto(
    val id: Long,
    val createDate: LocalDateTime,
    val title: String,
    val content: String,
    val isAnswer: Boolean


) {
    constructor(question: Question) : this(
        question.id,
        question.createDate,
        question.title,
        question.content,
        question.isAnswer
    )

    constructor(questionListDtoProjection: QuestionListDtoProjection) : this(
        questionListDtoProjection.getId(),
        questionListDtoProjection.getCreateDate(),
        questionListDtoProjection.getTitle(),
        questionListDtoProjection.getContent(),
        questionListDtoProjection.getIsAnswer()
    )
}