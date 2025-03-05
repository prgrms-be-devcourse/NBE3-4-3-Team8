package com.ll.nbe342team8.domain.qna.answer.controller

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.service.MemberService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.domain.qna.answer.dto.AnswerDto
import com.ll.nbe342team8.domain.qna.answer.dto.GetResAnswersDto
import com.ll.nbe342team8.domain.qna.answer.dto.GetResAnswersDto.Companion.from
import com.ll.nbe342team8.domain.qna.answer.dto.ReqAnswerDto
import com.ll.nbe342team8.domain.qna.answer.entity.Answer
import com.ll.nbe342team8.domain.qna.answer.service.AnswerService
import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.service.QuestionService
import com.ll.nbe342team8.global.exceptions.ServiceException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.Duration

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
@Tag(name = "Answer Controller", description = "관리자 전용 QnA 답변 API")
class AnswerController (
    private val memberService: MemberService,
    private val questionService: QuestionService,
    private val answerService: AnswerService
    ) {


    @Operation(summary = "사용자가 작성한 QnA 질문에 대한 답변 조회")
    @GetMapping("/questions/{questionId}/answers")
    fun getAnswers(
        @PathVariable questionId: Long,
        @AuthenticationPrincipal securityUser: SecurityUser
    ): ResponseEntity<GetResAnswersDto> {
        val member = getAuthenticatedMember(securityUser)
        val question = getQuestionById(questionId)

        // 질문 작성자 또는 관리자인지 확인
        validateQuestionOwner(member, question)

        val answers = answerService.findByQuestion(question)
        return ResponseEntity.ok(GetResAnswersDto.from(answers))
    }


    @Operation(summary = "사용자가 작성한 QnA 질문의 상세 답변 조회 (관리자 전용)")
    @GetMapping("/questions/{questionId}/answers/{answerId}")
    fun getAnswer(
        @PathVariable questionId: Long,
        @PathVariable answerId: Long,
        @AuthenticationPrincipal securityUser: SecurityUser
    ): ResponseEntity<AnswerDto> {
        val admin = getAuthenticatedMember(securityUser)
        validateAdmin(admin)

        val answer = getAnswerById(answerId)

        return ResponseEntity.ok(AnswerDto(answer))
    }



    @Operation(summary = "질문에 답변 등록 (관리자 전용)")
    @PostMapping("/questions/{questionId}/answers")
    fun postAnswer(
        @PathVariable questionId: Long,
        @RequestBody @Valid reqAnswerDto: ReqAnswerDto,
        @AuthenticationPrincipal securityUser: SecurityUser
    ): ResponseEntity<Void> {
        val admin = getAuthenticatedMember(securityUser)
        validateAdmin(admin)

        val question = getQuestionById(questionId)
        validateExistsDuplicateAnswerInShortTime(question, admin, reqAnswerDto.content, Duration.ofSeconds(5))

        answerService.createAnswer(question, admin, reqAnswerDto)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }


    @Operation(summary = "질문에 대한 답변 수정 (관리자 전용)")
    @PutMapping("/questions/{questionId}/answers/{answerId}")
    fun modifyAnswer(
        @PathVariable questionId: Long,
        @PathVariable answerId: Long,
        @RequestBody @Valid reqAnswerDto: ReqAnswerDto,
        @AuthenticationPrincipal securityUser: SecurityUser
    ): ResponseEntity<Void> {
        val admin = getAuthenticatedMember(securityUser)
        validateAdmin(admin)

        val question = getQuestionById(questionId)
        val answer = getAnswerById(answerId)

        answerService.modifyAnswer(answer, reqAnswerDto)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "질문에 대한 답변 삭제 (관리자 전용)")
    @DeleteMapping("/questions/{questionId}/answers/{answerId}")
    fun deleteAnswer(
        @PathVariable questionId: Long,
        @PathVariable answerId: Long,
        @AuthenticationPrincipal securityUser: SecurityUser
    ): ResponseEntity<Void> {
        val admin = getAuthenticatedMember(securityUser)
        validateAdmin(admin)

        val question = getQuestionById(questionId)
        val answer = getAnswerById(answerId)

        answerService.deleteAnswer(answer)
        return ResponseEntity.noContent().build()
    }


    /**
     * 🔥 공통 메서드들 (중복 코드 제거)
     */
    // 로그인된 사용자 정보 가져오기
    private fun getAuthenticatedMember(securityUser: SecurityUser?): Member {
        securityUser ?: throw ServiceException(HttpStatus.UNAUTHORIZED.value(), "로그인을 해야 합니다.")

        return memberService.findByOauthId(securityUser.member.oAuthId)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다.") }
    }

    // 질문 ID로 질문 가져오기
    private fun getQuestionById(questionId: Long): Question {
        return questionService.findById(questionId)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다.") }
    }

    // 답변 ID로 답변 가져오기
    private fun getAnswerById(answerId: Long): Answer {
        return answerService.findById(answerId)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "답변을 찾을 수 없습니다.") }
    }

    // 질문 작성자인지 확인
    private fun validateQuestionOwner(member: Member, question: Question) {
        if (!(questionService.isQuestionOwner(member, question) || checkAdmin(member))) {
            throw ServiceException(HttpStatus.FORBIDDEN.value(), "권한이 없습니다.")
        }
    }

    // 관리자 계정인지 확인
    private fun validateAdmin(member: Member) {
        if (!checkAdmin(member)) {
            throw ServiceException(HttpStatus.FORBIDDEN.value(), "권한이 없습니다.")
        }
    }

    // 짧은 시간 내 중복 답변 등록 방지
    private fun validateExistsDuplicateAnswerInShortTime(question: Question, member: Member, content: String, duration: Duration) {
        if (answerService.existsDuplicateAnswerInShortTime(question, member, content, duration)) {
            throw ServiceException(HttpStatus.TOO_MANY_REQUESTS.value(), "너무 빠르게 동일한 답변을 등록할 수 없습니다.")
        }
    }

    // 관리자 여부 체크
    private fun checkAdmin(member: Member): Boolean {
        return member.memberType == Member.MemberType.ADMIN
    }
}
