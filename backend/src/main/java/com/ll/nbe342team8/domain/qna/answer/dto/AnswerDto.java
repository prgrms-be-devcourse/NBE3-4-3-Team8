package com.ll.nbe342team8.domain.qna.answer.dto;

import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AnswerDto {

    @NotBlank
    Long id;

    LocalDateTime createDate;

    LocalDateTime modifyDate;

    private String content;

    public AnswerDto(Answer answer) {
        this.id=answer.getId();
        this.createDate=answer.getCreateDate();
        this.modifyDate=answer.getModifyDate();
        this.content=answer.getContent();
    }
}
