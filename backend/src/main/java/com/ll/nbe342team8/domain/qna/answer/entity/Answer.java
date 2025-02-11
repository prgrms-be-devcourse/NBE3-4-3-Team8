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
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;


    public void setQuestion(Question question) {
        this.question = question;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}