package com.ll.nbe342team8.domain.qna.answer.dto;

import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public record AnswerDto(
        Long id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        @NotNull String content
) {
    public AnswerDto(Answer answer) {
        this(
                answer.getId(),
                answer.getCreateDate(),
                answer.getModifyDate(),
                answer.getContent()
        );
    }
}
