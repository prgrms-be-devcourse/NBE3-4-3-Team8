package com.ll.nbe342team8.domain.qna.question.dto

import com.ll.nbe342team8.domain.qna.question.entity.Question
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

//질문 목록 조회시 사용
data class QuestionListDto(
    val id: @NotNull Long?,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val title: String,
    val content: String?,
    val isAnswer: Boolean?


) {
    constructor(question: Question) : this(
        question.id,
        question.createDate,
        question.modifyDate,
        question.title,
        question.content,
        question.isAnswer
    )

    constructor(questionListDtoProjection: QuestionListDtoProjection) : this(
        questionListDtoProjection.getId(),
        questionListDtoProjection.getCreateDate(),
        questionListDtoProjection.getModifyDate(),
        questionListDtoProjection.getTitle(),
        questionListDtoProjection.getContent(),
        questionListDtoProjection.getIsAnswer()
    )
}
