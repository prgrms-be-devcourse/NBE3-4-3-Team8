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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    public Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    public String content;


    @ManyToOne(fetch = FetchType.LAZY)
    public Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    public Member member;

    public void updateAnswerInfo(ReqAnswerDto dto) {
        this.content=dto.getContent();
    }

    // Private constructor to enforce the use of the from method
    private Answer(String content, Member member, Question question) {
        this.content = content;
        this.member = member;
        this.question = question;
    }

    // Static from method to create an Answer instance
    public static Answer from(String content, Member member, Question question) {
        return new Answer(content, member, question);
    }

}