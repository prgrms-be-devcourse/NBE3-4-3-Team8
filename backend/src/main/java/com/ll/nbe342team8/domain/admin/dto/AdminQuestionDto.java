package com.ll.nbe342team8.domain.admin.dto;

import com.ll.nbe342team8.domain.qna.answer.dto.AnswerDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import lombok.Getter;

@Getter
public class AdminQuestionDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String memberEmail;
    private final String createDate;
    private final boolean hasAnswer;
    private final AnswerDto answer;

    public AdminQuestionDto(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.memberEmail = question.getMember().getEmail();
        this.createDate = question.getCreateDate().toString();
        this.hasAnswer = !question.getAnswers().isEmpty();
        this.answer = question.getAnswers().isEmpty() ? null : new AnswerDto(question.getAnswers().getFirst());
    }
}