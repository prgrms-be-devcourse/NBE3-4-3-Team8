package com.ll.nbe342team8.domain.qna.question.entity;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import com.ll.nbe342team8.standard.util.Ut;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
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