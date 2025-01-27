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

}
