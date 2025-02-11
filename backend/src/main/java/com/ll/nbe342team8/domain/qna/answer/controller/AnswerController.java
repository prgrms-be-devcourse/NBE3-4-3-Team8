package com.ll.nbe342team8.domain.qna.answer.controller;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.qna.answer.dto.AnswerDto;
import com.ll.nbe342team8.domain.qna.answer.dto.GetResAnswersDto;
import com.ll.nbe342team8.domain.qna.answer.dto.ReqAnswerDto;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.answer.service.AnswerService;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.service.QuestionService;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
public class AnswerController {

    private final MemberService memberService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    //qna 답변 경우 관리자만 작성 가능하다

    @Operation(summary = "사용자가 작성한 qna 질문에 대한 답변 조회")
    @GetMapping("/question/{questionId}/answer")
    public ResponseEntity<?> getAnswers(@PathVariable Long questionId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다."));

        //프론트에서 보낸 질문id가 사용자가 작성한지 확인
        validateQuestionOwner(member, question);

        //답변 데이터를 가져와 dto로 변환
        List<Answer> answers =answerService.findByQuestion(question);
        GetResAnswersDto getResAnswersDto=new GetResAnswersDto(answers);

        return ResponseEntity.ok(getResAnswersDto);
    }

    //관리자가 상세 답변 데이터 조회
    @Operation(summary = "사용자가 작성한 qna 질문의 상세 답변 조회")
    @GetMapping("/question/{questionId}/answer/{answerId}")
    public ResponseEntity<?> getAnswer(@PathVariable Long questionId,
                                       @PathVariable Long answerId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member member = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다."));

        Answer answer=answerService.findById(answerId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "답변을 찾을 수 없습니다."));


        //관리자 계정인 경우 누구던 답변 등록 가능, 아닌 경우 에러 발생
        validateAdmin(member);

        //답변 데이터를 가져와 dto로 변환
        AnswerDto answerDto=new AnswerDto(answer);

        return ResponseEntity.ok(answerDto);
    }


    //관리자가 질문에 답변 등록
    @Operation(summary = "사용자가 작성한 qna 질문에 답변 등록(관리자 전용)")
    @PostMapping("/question/{questionId}/answer")
    public ResponseEntity<?> postAnswer(@PathVariable Long questionId,
                                        @RequestBody @Valid ReqAnswerDto reqAnswerDto ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member admin = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다."));


        //관리자 계정인 경우 누구던 답변 등록 가능, 아닌 경우 에러 발생
        validateAdmin(admin);
        //짫은 시간 내 중복 등록 방지
        validateExistsDuplicateAnswerInShortTime(question, admin, reqAnswerDto.content(), Duration.ofSeconds(5));

        //답변 등록
        answerService.createAnswer(question,admin,reqAnswerDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "답변 등록 성공."));
    }

    //관리자가 질문에 답변 수정
    @Operation(summary = "사용자가 작성한 qna 질문에 답변 수정(관리자 전용)")
    @PutMapping("/question/{questionId}/answer/{answerId}")
    public ResponseEntity<?> modifyAnswer(@PathVariable Long questionId,
                                          @PathVariable Long answerId,
                                          @RequestBody @Valid ReqAnswerDto reqAnswerDto ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member admin = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다."));

        Answer answer=answerService.findById(answerId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "답변을 찾을 수 없습니다."));

        //관리자 계정 확인
        validateAdmin(admin);

        //답변 수정
        answerService.modifyAnswer(answer,reqAnswerDto);

        return ResponseEntity.ok(Map.of("message", "답변 수정 성공."));
    }

    //관리자가 답변 삭제
    @Operation(summary = "사용자가 작성한 qna 질문에 답변 삭제(관리자 전용)")
    @DeleteMapping("/question/{questionId}/answer/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long questionId,
                                          @PathVariable Long answerId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal()  instanceof SecurityUser securityUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"로그인을 해야합니다.");
        }

        String oauthId=securityUser.getMember().getOAuthId();

        Member admin = memberService.findByOauthId(oauthId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));

        Question question = questionService.findById(questionId)
                .orElseThrow(() -> new ServiceException(404, "질문을 찾을 수 없습니다."));

        Answer answer=answerService.findById(answerId)
                .orElseThrow(() -> new ServiceException(404, "답변을 찾을 수 없습니다."));


        validateAdmin(admin);

        //답변 삭제
        answerService.deleteAnswer(answer);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "답변 수정 성공."));
    }

    //사용자 권한 확인, 관리자 계정이여도 접근 가능
    private void validateQuestionOwner(Member member, Question question) {
        if (!(questionService.isQuestionOwner(member, question) || checkAdmin(member))) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), "권한이 없습니다.");
        }
    }


    // 관리자 계정 확인
    private void validateAdmin(Member member) {
        if(!checkAdmin(member)) {
            throw new ServiceException(HttpStatus.FORBIDDEN.value(), "권한이 없습니다.");
        }
    }

    private void validateExistsDuplicateAnswerInShortTime(Question question,Member member, String content, Duration duration) {
        if (answerService.existsDuplicateAnswerInShortTime(question, member, content, duration)) {
            throw new ServiceException(HttpStatus.TOO_MANY_REQUESTS.value(), "너무 빠르게 동일한 답변을 등록할 수 없습니다.");
        }
    }

    private boolean checkAdmin(Member member) {
        return member.getMemberType() == Member.MemberType.ADMIN;
    }
}