package com.ll.nbe342team8.domain.qna.answer.entity;

import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer extends BaseTime {
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;


    public void setQuestion(Question question) {
        // 기존 관계 제거
        if (this.question != null) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;
        // 새로운 관계 설정
        if (question != null && !question.getAnswers().contains(this)) {
            question.getAnswers().add(this);
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }
}