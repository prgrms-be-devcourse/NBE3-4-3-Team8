package com.ll.nbe342team8.domain.qna.question.entity;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
<<<<<<< HEAD
import com.ll.nbe342team8.standard.util.Ut;
=======
>>>>>>> fd94d04b325396805d818486d9c0e8c7a48cf3c7
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

    @Column(nullable = true) // 질문 제목
    private String title;

    @Column(nullable = true) // 질문 내용
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers;


    public void updateQuestionInfo(ReqQuestionDto dto) {
        this.title= Ut.XSSSanitizer.sanitize(dto.getTitle());
        this.content=Ut.XSSSanitizer.sanitize(dto.getContent());
    }

}
