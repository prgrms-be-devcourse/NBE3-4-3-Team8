package com.ll.nbe342team8.domain.qna.question.entity;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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


    @Column(nullable = true) // 질문 제목
    private String questionTitle;

    @Column(nullable = true) // 질문 내용
    private String questionContent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;



}
