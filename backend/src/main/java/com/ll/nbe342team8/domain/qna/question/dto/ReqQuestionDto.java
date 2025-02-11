package com.ll.nbe342team8.domain.qna.question.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record ReqQuestionDto(
        @NotBlank String title,
        @NotNull String content
) {}