package com.ll.nbe342team8.domain.qna.answer.controller;

import java.time.Duration;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
@Tag(name = "Answer Controller", description = "관리자 전용 QnA 답변 API")
public class AnswerController {

	private final MemberService memberService;
	private final QuestionService questionService;
	private final AnswerService answerService;

	@Operation(summary = "사용자가 작성한 QnA 질문에 대한 답변 조회")
	@GetMapping("/question/{questionId}/answer")
	public ResponseEntity<GetResAnswersDto> getAnswers(
			@PathVariable Long questionId,
			@AuthenticationPrincipal SecurityUser securityUser
	) {
		Member member = getAuthenticatedMember(securityUser);
		Question question = getQuestionById(questionId);

		// 질문 작성자 또는 관리자인지 확인
		validateQuestionOwner(member, question);

		List<Answer> answers = answerService.findByQuestion(question);
		return ResponseEntity.ok(new GetResAnswersDto(answers));
	}

	@Operation(summary = "사용자가 작성한 QnA 질문의 상세 답변 조회 (관리자 전용)")
	@GetMapping("/questions/{questionId}/answers/{answerId}")
	public ResponseEntity<AnswerDto> getAnswer(
			@PathVariable Long questionId,
			@PathVariable Long answerId,
			@AuthenticationPrincipal SecurityUser securityUser
	) {
		Member admin = getAuthenticatedMember(securityUser);
		validateAdmin(admin);

		Question question = getQuestionById(questionId);
		Answer answer = getAnswerById(answerId);

		return ResponseEntity.ok(new AnswerDto(answer));
	}

	@Operation(summary = "질문에 답변 등록 (관리자 전용)")
	@PostMapping("/questions/{questionId}/answers")
	public ResponseEntity<Void> postAnswer(
			@PathVariable Long questionId,
			@RequestBody @Valid ReqAnswerDto reqAnswerDto,
			@AuthenticationPrincipal SecurityUser securityUser
	) {
		Member admin = getAuthenticatedMember(securityUser);
		validateAdmin(admin);

		Question question = getQuestionById(questionId);
		validateExistsDuplicateAnswerInShortTime(question, admin, reqAnswerDto.content(), Duration.ofSeconds(5));

		answerService.createAnswer(question, admin, reqAnswerDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "질문에 대한 답변 수정 (관리자 전용)")
	@PutMapping("/questions/{questionId}/answers/{answerId}")
	public ResponseEntity<Void> modifyAnswer(
			@PathVariable Long questionId,
			@PathVariable Long answerId,
			@RequestBody @Valid ReqAnswerDto reqAnswerDto,
			@AuthenticationPrincipal SecurityUser securityUser
	) {
		Member admin = getAuthenticatedMember(securityUser);
		validateAdmin(admin);

		Question question = getQuestionById(questionId);
		Answer answer = getAnswerById(answerId);

		answerService.modifyAnswer(answer, reqAnswerDto);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "질문에 대한 답변 삭제 (관리자 전용)")
	@DeleteMapping("/questions/{questionId}/answers/{answerId}")
	public ResponseEntity<Void> deleteAnswer(
			@PathVariable Long questionId,
			@PathVariable Long answerId,
			@AuthenticationPrincipal SecurityUser securityUser
	) {
		Member admin = getAuthenticatedMember(securityUser);
		validateAdmin(admin);

		Question question = getQuestionById(questionId);
		Answer answer = getAnswerById(answerId);

		answerService.deleteAnswer(answer);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 🔥 공통 메서드들 (중복 코드 제거)
	 */

	// 로그인된 사용자 정보 가져오기
	private Member getAuthenticatedMember(SecurityUser securityUser) {
		if (securityUser == null) {
			throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), "로그인을 해야 합니다.");
		}

		return memberService.findByOauthId(securityUser.getMember().getOAuthId())
				.orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."));
	}

	// 질문 ID로 질문 가져오기
	private Question getQuestionById(Long questionId) {
		return questionService.findById(questionId)
				.orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다."));
	}

	// 답변 ID로 답변 가져오기
	private Answer getAnswerById(Long answerId) {
		return answerService.findById(answerId)
				.orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND.value(), "답변을 찾을 수 없습니다."));
	}

	// 질문 작성자인지 확인
	private void validateQuestionOwner(Member member, Question question) {
		if (!(questionService.isQuestionOwner(member, question) || checkAdmin(member))) {
			throw new ServiceException(HttpStatus.FORBIDDEN.value(), "권한이 없습니다.");
		}
	}

	// 관리자 계정인지 확인
	private void validateAdmin(Member member) {
		if (!checkAdmin(member)) {
			throw new ServiceException(HttpStatus.FORBIDDEN.value(), "권한이 없습니다.");
		}
	}

	// 짧은 시간 내 중복 답변 등록 방지
	private void validateExistsDuplicateAnswerInShortTime(Question question, Member member, String content, Duration duration) {
		if (answerService.existsDuplicateAnswerInShortTime(question, member, content, duration)) {
			throw new ServiceException(HttpStatus.TOO_MANY_REQUESTS.value(), "너무 빠르게 동일한 답변을 등록할 수 없습니다.");
		}
	}

	// 관리자 여부 체크
	private boolean checkAdmin(Member member) {
		return member.getMemberType() == Member.MemberType.ADMIN;
	}
}
