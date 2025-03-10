package com.ll.nbe342team8.domain.qna.answer.entity;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.answer.dto.ReqAnswerDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import com.ll.nbe342team8.standard.util.Ut;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void updateAnswerInfo(ReqAnswerDto dto) {
        this.content=dto.content();
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}