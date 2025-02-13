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
		this.memberEmail = (question.getMember() != null) ? question.getMember().getEmail() : "탈퇴한 회원";
		this.createDate = question.getCreateDate().toString();
		this.hasAnswer = question.getAnswers() != null && !question.getAnswers().isEmpty();
		this.answer = (this.hasAnswer) ? new AnswerDto(question.getAnswers().getFirst()) : null;
	}
}
