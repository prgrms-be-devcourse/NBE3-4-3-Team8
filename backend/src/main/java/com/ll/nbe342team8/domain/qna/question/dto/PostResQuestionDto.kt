package com.ll.nbe342team8.domain.qna.question.dto;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import java.util.List;
import java.util.stream.Collectors;

public record PostResQuestionDto(List<QuestionDto> questions) {
    public PostResQuestionDto(Member member) {
        this(List.copyOf(member.getQuestions().stream()
                .map(QuestionDto::new)
                .collect(Collectors.toList()))); // 🔥 불변 리스트 적용
    }
}