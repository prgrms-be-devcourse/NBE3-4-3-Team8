package com.ll.nbe342team8.domain.qna.answer.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.ll.nbe342team8.domain.qna.answer.entity.Answer

data class GetResAnswersDto(
    @JsonProperty("answers") val answers: List<AnswerDto>
) {
    companion object {
        fun from(answers: List<Answer>): GetResAnswersDto {
            return GetResAnswersDto(answers.map { AnswerDto(it) })
        }
    }
}