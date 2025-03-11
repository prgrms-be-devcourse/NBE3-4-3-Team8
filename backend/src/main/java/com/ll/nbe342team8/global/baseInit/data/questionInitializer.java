package com.ll.nbe342team8.global.baseInit.data;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.domain.qna.question.service.QuestionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/*
@Component
@RequiredArgsConstructor
public class questionInitializer {
    private final MemberRepository memberRepository;
    private final QuestionService questionService;
    @PostConstruct
    public void init() {
        String email = "rdh0427@naver.com";
        Member member = memberRepository.findByEmail(email).get();

        for(int i=0;i<10000;i++) {
            ReqQuestionDto reqQuestionDto=new ReqQuestionDto("title"+i,"content"+i);
            questionService.createQuestion(member,reqQuestionDto);

        }
    }
}
*/

