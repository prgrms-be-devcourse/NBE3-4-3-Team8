package com.ll.nbe342team8.domain.qna.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionDto {

    @NotBlank
    @JsonProperty("title")
    String title;

    @JsonProperty("content")
    String content;
}
