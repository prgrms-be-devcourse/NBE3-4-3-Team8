package com.ll.nbe342team8.domain.qna.question.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist

data class ReqQuestionDto @JsonCreator constructor(
    @field:NotBlank
    val title: String,
    @field:NotNull
    val content: String
) {
    val cleanTitle: String? = title.let { Jsoup.clean(it, Safelist.basic()) }
    val cleanContent: String? = content.let { Jsoup.clean(it, Safelist.basic()) }
}

