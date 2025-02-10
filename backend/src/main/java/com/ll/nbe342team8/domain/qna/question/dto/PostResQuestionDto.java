package com.ll.nbe342team8.domain.qna.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record PostResQuestionDto(List<QuestionDto> questions) {
    public PostResQuestionDto(Member member) {
        this(List.copyOf(member.getQuestions().stream()
                .map(QuestionDto::new)
                .collect(Collectors.toList()))); // 🔥 불변 리스트 적용
    }
}