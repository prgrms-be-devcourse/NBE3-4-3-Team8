package com.ll.nbe342team8.domain.qna.question.dto;

import com.ll.nbe342team8.domain.qna.question.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;

public record QuestionDto(
        @NotNull Long id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        @NotBlank String title,
        @NotNull String content
) {
    public QuestionDto(Question question) {
        this(
                question.getId(),
                question.getCreateDate(),
                question.getModifyDate(),
                question.getTitle(),
                question.getContent()
        );
    }
}