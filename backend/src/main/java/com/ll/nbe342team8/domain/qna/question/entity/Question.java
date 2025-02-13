package com.ll.nbe342team8.domain.qna.question.entity;

import java.util.List;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import com.ll.nbe342team8.standard.util.Ut;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	private Long id;

	@Column(nullable = true) // 질문 제목
	private String title;

	@Column(nullable = true) // 질문 내용
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false) // 회원이 반드시 존재해야 한다.
	private Member member;


	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
	private List<Answer> answers;

  public void updateQuestionInfo(ReqQuestionDto dto) {
      this.title= Ut.XSSSanitizer.sanitize(dto.title());
      this.content=Ut.XSSSanitizer.sanitize(dto.content());
  }

	public void addAnswer(Answer answer) {
		this.answers.add(answer);
		if (answer.getQuestion() != this) {
			answer.setQuestion(this);
		}
	}

	public void removeAnswer(Answer answer) {
		this.answers.remove(answer);
		answer.setQuestion(null);
	}
}
