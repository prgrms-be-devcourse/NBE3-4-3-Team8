package com.ll.nbe342team8.domain.qna.answer.dto;

import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.question.dto.QuestionDto;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

public class GetResAnswersDto {

    List<AnswerDto> answers;

    public GetResAnswersDto(List<Answer> answers) {

        this.answers= answers.stream()
                        .map(AnswerDto::new)
                        .collect(Collectors.toList());
    }
}
