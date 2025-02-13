package com.ll.nbe342team8.domain.admin.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.nbe342team8.domain.admin.dto.AdminQuestionDto;
import com.ll.nbe342team8.domain.admin.service.AdminQuestionService;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.answer.repository.AnswerRepository;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository;
import com.ll.nbe342team8.standard.PageDto.PageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
@Slf4j
public class AdminQuestionController {

	private final AdminQuestionService adminQuestionService;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;

	// 전체 질문 조회
	@GetMapping("/questions")
	public ResponseEntity<PageDto<AdminQuestionDto>> getAdminQuestions(
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Boolean hasAnswer,
			@PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {

		PageDto<AdminQuestionDto> questions = adminQuestionService.getQuestionsForAdmin(keyword, hasAnswer, pageable);
		return ResponseEntity.ok(questions);
	}

	// 특정 질문 조회
	@GetMapping("/questions/{id}")
	public ResponseEntity<?> getAdminQuestion(@PathVariable Long id) {
		Question question = adminQuestionService.getQuestionById(id);
		if (question == null) {
			return ResponseEntity.notFound().build();
		}
		AdminQuestionDto dto = new AdminQuestionDto(question);

		return ResponseEntity.ok(dto);
	}

	// 질문 삭제
	@DeleteMapping("/questions/{questionid}")
	@Transactional
	public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionid) {

		Question question = questionRepository.findById(questionid)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다: " + questionid));

		List<Answer> answers = answerRepository.findByQuestionId(questionid);
		answerRepository.deleteAll(answers);

		questionRepository.delete(question);

		return ResponseEntity.ok().build();
	}

}
