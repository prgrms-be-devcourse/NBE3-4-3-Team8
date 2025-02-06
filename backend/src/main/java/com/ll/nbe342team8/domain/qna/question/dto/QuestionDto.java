package com.ll.nbe342team8.domain.qna.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class QuestionDto {

    @NotBlank
    Long id;

    LocalDateTime createDate;

    LocalDateTime modifyDate;

    @NotBlank
    @JsonProperty("title")
    String title;

    @NotBlank
    @JsonProperty("content")
    String content;

    public QuestionDto (Question question) {
        this.id=question.getId();
        this.title=question.getTitle();
        this.content= question.getContent();
        this.createDate=question.getCreateDate();
        this.modifyDate=question.getModifyDate();
    }
}
