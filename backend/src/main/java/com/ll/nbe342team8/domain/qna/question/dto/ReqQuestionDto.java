package com.ll.nbe342team8.domain.qna.question.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;


public record ReqQuestionDto(
        @NotBlank String title,
        @NotNull String content
) {
        @JsonCreator
        public ReqQuestionDto(
                @JsonProperty("title") String title,
                @JsonProperty("content") String content
        ) {
                this.title = Jsoup.clean(title, Safelist.basic());
                this.content = Jsoup.clean(content, Safelist.basic());
        }
}
