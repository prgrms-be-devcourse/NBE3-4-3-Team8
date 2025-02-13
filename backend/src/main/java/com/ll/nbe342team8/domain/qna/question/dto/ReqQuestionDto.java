package com.ll.nbe342team8.domain.qna.question.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ReqQuestionDto(
        @NotBlank String title,
        @NotNull String content
) {}