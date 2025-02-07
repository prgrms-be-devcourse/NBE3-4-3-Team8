package com.ll.nbe342team8.domain.qna.question.controller;

import com.ll.nbe342team8.domain.member.member.dto.ResMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.qna.question.dto.QuestionDto;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.service.QuestionService;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import com.ll.nbe342team8.standard.PageDto.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Tag(name = "QuestionController", description = " qna 질문 컨트롤러")
@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final MemberService memberService;
    private final QuestionService questionService;

    @Operation(summary = "사용자가 작성한 qna 질문 목록 조회")
    @GetMapping("/my/question")
    public ResponseEntity<?> getQuesitons(@RequestParam(name = "page", defaultValue = "0") int page
    ) {

        //jwt 토큰에서 id를 통해 회원정보를 찾는다.
        //여기선 임시로 이메일을 통해 회원정보를 찾는다.
        String email="rdh0427@naver.com";

        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new ServiceException(404, "사용자를 찾을 수 없습니다."));

        PageDto<QuestionDto> pageDto = new PageDto<>();

        pageDto = questionService.getPage(member, page);

        return ResponseEntity.ok(pageDto);
    }

    @Operation(summary = "사용자의 특정 qna 질문 조회")
    @GetMapping("/my/question/{id}")
    public ResponseEntity<?> getQuestion(@RequestParam(name = "id") Long id) {

        String email="rdh0427@naver.com";

        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new ServiceException(404, "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(id)
                .orElseThrow(() -> new ServiceException(404, "질문을 찾을 수 없습니다."));

        QuestionDto questionDto=new QuestionDto(question);

        return ResponseEntity.ok(questionDto);
    }

    @Operation(summary = "사용자가 qna 질문 등록")
    @PostMapping("/my/question")
    public ResponseEntity<?> postQuesiton(@RequestBody @Valid ReqQuestionDto reqQuestionDto
                                          ) {

        //jwt 토큰에서 id를 통해 회원정보를 찾는다.
        //여기선 임시로 이메일을 통해 회원정보를 찾는다.
        String email="rdh0427@naver.com";

        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new ServiceException(404, "사용자를 찾을 수 없습니다."));

        validateExistsDuplicateQuestionInShortTime(member, reqQuestionDto.getTitle(),reqQuestionDto.getContent(),Duration.ofSeconds(5));
        questionService.createQuestion(member,reqQuestionDto);

        return ResponseEntity.ok(Map.of("message", "질문 등록 성공."));
    }

    @Operation(summary = "사용자의 특정 qna 질문 수정")
    @PutMapping("/my/question/{id}")
    public ResponseEntity<?> modifyQuesiton(@RequestParam(name = "id") Long id
            ,@RequestBody @Valid ReqQuestionDto reqQuestionDto) {

        //jwt 토큰에서 id를 통해 회원정보를 찾는다.
        //여기선 임시로 이메일을 통해 회원정보를 찾는다.
        String email="rdh0427@naver.com";

        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new ServiceException(404, "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(id)
                .orElseThrow(() -> new ServiceException(404, "질문을 찾을 수 없습니다."));

        validateQuestionOwner(member, question);

        questionService.modifyQuestion(question,reqQuestionDto);

        return ResponseEntity.ok(Map.of("message", "질문 수정 성공."));
    }

    @Operation(summary = "사용자의 특정 qna 질문 삭제")
    @DeleteMapping("/my/question/{id}")
    public ResponseEntity<?> removeQuesiton(@RequestParam(name = "id") Long id
    ) {

        //jwt 토큰에서 id를 통해 회원정보를 찾는다.
        //여기선 임시로 이메일을 통해 회원정보를 찾는다.
        String email="rdh0427@naver.com";

        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new ServiceException(404, "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(id)
                .orElseThrow(() -> new ServiceException(404, "질문을 찾을 수 없습니다."));

        validateQuestionOwner(member, question);

        questionService.deleteQuestion(question);

        return ResponseEntity.ok(Map.of("message", "질문 삭제 성공."));
    }



    //사용자 권한 확인, 관리자 계정이여도 접근 가능
    private void validateQuestionOwner(Member member, Question question) {
        if (!(questionService.isQuestionOwner(member, question) || checkAdmin(member))) {
            throw new ServiceException(403, "권한이 없습니다.");
        }
    }


    private void validateExistsDuplicateQuestionInShortTime(Member member,String title, String content, Duration duration) {
        if (questionService.existsDuplicateQuestionInShortTime(member,title, content, duration)) {
            throw new ServiceException(400, "너무 빠르게 동일한 답변을 등록할 수 없습니다.");
        }
    }

    private boolean checkAdmin(Member member) {
        return member.getMemberType() == Member.MemberType.ADMIN;
    }
}
