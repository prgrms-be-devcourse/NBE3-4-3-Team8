package com.ll.nbe342team8.domain.qna.question.controller;

import com.ll.nbe342team8.domain.member.member.dto.ResMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.domain.qna.question.service.QuestionService;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final MemberService memberService;
    private final QuestionService questionService;

    @PostMapping("/my/question")
    public ResponseEntity<?> postQuesiton(@RequestBody @Valid ReqQuestionDto reqQuestionDto
                                          ) {

        //jwt 토큰에서 id를 통해 회원정보를 찾는다.
        //여기선 임시로 이메일을 통해 회원정보를 찾는다.
        String email="rdh0427@naver.com";

        Optional<Member> optionalMember = memberService.findByEmail(email);

        //이메일에 대응하는 사용자가 없는 경우 에러 발생
        if(optionalMember.isEmpty()) { throw new ServiceException(404,"사용자를 찾을 수 없습니다.");}

        Member member=optionalMember.get();

        questionService.createQuestion(member,reqQuestionDto);

        //갱신된 사용자 개체를 dto로 변환해 반환한다. 프론트에선 반환 받는 memberDto로 마이페이지 갱신
        ResMemberMyPageDto resMemberMyPageDto=new ResMemberMyPageDto(member);
        return ResponseEntity.status(200).body(resMemberMyPageDto);
    }
}
