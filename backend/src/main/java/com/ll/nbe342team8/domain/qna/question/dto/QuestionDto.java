package com.ll.nbe342team8.domain.qna.question.dto;

import com.ll.nbe342team8.domain.qna.answer.dto.AnswerDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import jakarta.validation.constraints.NotNull;



import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record QuestionDto(

        @NotNull Long id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        String title,
        String content,
        Boolean isAnswer,
        List<AnswerDto> answers,
        List<QuestionGenFileDto> genFiles
) {
    public QuestionDto(Question question) {

        this(
                question.getId(),
                question.getCreateDate(),
                question.getModifyDate(),
                question.getTitle(),
                question.getContent(),
                question.getAnswers() != null && !question.getAnswers().isEmpty(),
                question.getAnswers() != null
                        ? question.getAnswers().stream()
                        .map(AnswerDto::new) // ✅ List<Answer> → List<AnswerDto> 변환
                        .collect(Collectors.toList())
                        : List.of(), // null 방지 (빈 리스트 반환)
                question.getGenFiles()!= null
                        ? question.getGenFiles().stream()
                        .map(QuestionGenFileDto::new) // ✅ List<Answer> → List<AnswerDto> 변환
                        .collect(Collectors.toList())
                        : List.of()
        );
    }
}