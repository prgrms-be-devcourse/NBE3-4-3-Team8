package com.ll.nbe342team8.domain.qna.question.controller

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.service.MemberService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.domain.qna.question.dto.QuestionDto
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDto
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto
import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.service.QuestionService
import com.ll.nbe342team8.global.exceptions.ServiceException
import com.ll.nbe342team8.standard.PageDto.PageDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.time.Duration


@Tag(name = "QuestionController", description = " qna 질문 컨트롤러")
@RequiredArgsConstructor
@RestController
class QuestionController(
    private val memberService: MemberService,
    private val questionService: QuestionService
)  {


    //20 ~30ms 데이터 100개 이하일때
    // 30 ~ 50ms 데이터 10000개 일때
    @Operation(summary = "사용자가 작성한 qna 질문 목록 조회")
    @GetMapping("/my/question")
    fun getQuestions(@RequestParam(name = "page", defaultValue = "0") page: Int,
                     @AuthenticationPrincipal securityUser :SecurityUser?): ResponseEntity<PageDto<QuestionListDto>> {
        val member: Member = securityUser?.member
        ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        val pageDto = questionService.getPage(member, page)
        return ResponseEntity.ok(pageDto)
    }


    /*
    //33ms , 62ms 데이터 100개 이하일때
    //20 ~30ms 데이터 10000개 일때
    //조회 데이터가 적을땐 효율이 좋지않음
    //CREATE INDEX idx_question_member_create_date_id ON question (member_id, createDate DESC, id DESC);
    //복합 인덱스 추가로 개선 가능
    @Operation(summary = "사용자가 작성한 QnA 질문 목록 조회")
    @GetMapping("/my/question")
    fun getKeySetQuestions(
        @RequestParam(name = "page", required = false) page: Int?,
        @RequestParam(name = "lastQuestionId", required = false) lastQuestionId: Long?,
        @RequestParam(name = "firstQuestionId", required = false) firstQuestionId: Long?,
        @AuthenticationPrincipal securityUser :SecurityUser?
    ): ResponseEntity<PageDto<QuestionListDto>> {

        val member: Member = securityUser?.member
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        // 현재 페이지의 바로 다음 페이지로 이동할 경우 keyset 방식을 이용해 이동합니다
        // 그외 임의 페이지로 점프하는 경우 원래 대로 offset을 이용해 이동합니다.
        val pageDto = when {
            lastQuestionId != null || firstQuestionId != null ->
                questionService.getNextOrBeforePage(member, lastQuestionId, firstQuestionId)
            page != null ->
                questionService.getPage(member, page)
            else -> throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다.")
        }

        return ResponseEntity.ok(pageDto)
    }

     */




    @Operation(summary = "사용자의 특정 qna 질문 조회")
    @GetMapping("/my/question/{id}")
    fun getQuestion(@PathVariable id: Long, @AuthenticationPrincipal securityUser :SecurityUser?): ResponseEntity<QuestionDto> {
        val member: Member = securityUser?.member
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        val question = questionService.findById(id)

        questionService.validateQuestionOwner(member, question)

        val questionDto = QuestionDto(question)

        return ResponseEntity.ok(questionDto)
    }


    @Operation(summary = "사용자가 qna 질문 등록")
    @PostMapping("/my/question")
    fun postQuestion(
        @RequestBody reqQuestionDto: @Valid ReqQuestionDto,@AuthenticationPrincipal securityUser :SecurityUser?
    ): ResponseEntity<QuestionDto> {
        val member: Member = securityUser?.member
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        questionService.validateExistsDuplicateQuestionInShortTime( member,reqQuestionDto.title ?: "",reqQuestionDto.content ?: "", Duration.ofSeconds(5))
        val question = questionService.createQuestion(member, reqQuestionDto)

        val questionDto = QuestionDto(question)

        return ResponseEntity.status(HttpStatus.CREATED).body(questionDto)
    }

    @Operation(summary = "사용자의 특정 qna 질문 수정")
    @PutMapping("/my/question/{id}")
    fun putQuestion(
        @PathVariable id: Long,
        @RequestBody reqQuestionDto: @Valid ReqQuestionDto, @AuthenticationPrincipal securityUser :SecurityUser?
    ): ResponseEntity<Void> {
        val member: Member = securityUser?.member
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        val question = questionService.findById(id)

        questionService.validateQuestionOwner(member, question)

        questionService.modifyQuestion(question, reqQuestionDto)

        return ResponseEntity.ok().build()
    }



    @Operation(summary = "사용자의 특정 qna 질문 삭제")
    @DeleteMapping("/my/question/{id}")
    fun deleteQuestion(
        @PathVariable id: Long, @AuthenticationPrincipal securityUser :SecurityUser?
    ): ResponseEntity<Void> {
        val member: Member = securityUser?.member
            ?: throw ServiceException(HttpStatus.BAD_REQUEST.value(), "올바른 요청이 아닙니다. 로그인 상태를 확인하세요.")

        val question = questionService.findById(id)

        questionService.validateQuestionOwner(member, question)

        questionService.deleteQuestion(question)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }




}

