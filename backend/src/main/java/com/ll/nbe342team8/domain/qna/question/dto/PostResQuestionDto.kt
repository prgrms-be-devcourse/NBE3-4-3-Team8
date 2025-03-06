package com.ll.nbe342team8.domain.qna.question.dto

import com.ll.nbe342team8.domain.member.member.entity.Member


data class PostResQuestionDto( val questions: List<QuestionDto>) {
    constructor(member: Member) : this(
        member.questions.map { QuestionDto(it) }.toList()
    )
}