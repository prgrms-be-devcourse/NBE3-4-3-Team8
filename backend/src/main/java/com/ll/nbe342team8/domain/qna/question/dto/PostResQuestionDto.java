package com.ll.nbe342team8.domain.qna.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostResQuestionDto {

    @NotBlank
    @JsonProperty("title")
    private String title;

    @NotNull
    private String content;

    public PostResQuestionDto(Member member) {
    }
}
