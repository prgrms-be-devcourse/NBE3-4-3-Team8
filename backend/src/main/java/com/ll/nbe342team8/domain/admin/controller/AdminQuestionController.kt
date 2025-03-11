package com.ll.nbe342team8.domain.admin.controller
/*
import com.ll.nbe342team8.domain.admin.dto.AdminQuestionDto
import com.ll.nbe342team8.domain.admin.service.AdminQuestionService
import com.ll.nbe342team8.domain.qna.answer.repository.AnswerRepository
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository
import com.ll.nbe342team8.standard.PageDto.PageDto
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/dashboard")
class AdminQuestionController(
    private val adminQuestionService: AdminQuestionService,
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository
) {

    // 전체 질문 조회
    @GetMapping("/questions")
    fun getAdminQuestions(
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false) hasAnswer: Boolean?,
        @PageableDefault(
            size = 10,
            sort = ["createDate"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<PageDto<AdminQuestionDto>> {
        val questions = adminQuestionService.getQuestionsForAdmin(keyword, hasAnswer, pageable)
        return ResponseEntity.ok(questions)
    }

    // 특정 질문 조회
    @GetMapping("/questions/{id}")
    fun getAdminQuestion(@PathVariable id: Long): ResponseEntity<*> {
        val question = adminQuestionService.getQuestionById(id)
            ?: return ResponseEntity.notFound().build<Void>()

        val dto = AdminQuestionDto(question)
        return ResponseEntity.ok(dto)
    }

    // 질문 삭제
    @DeleteMapping("/questions/{questionid}")
    @Transactional
    fun deleteQuestion(@PathVariable questionid: Long): ResponseEntity<Void> {
        val question = questionRepository.findById(questionid)
            .orElseThrow { IllegalArgumentException("존재하지 않는 질문입니다: $questionid") }

        val answers = answerRepository.findByQuestionId(questionid)
        answerRepository.deleteAll(answers)

        questionRepository.delete(question)

        return ResponseEntity.ok().build()
    }
}*/