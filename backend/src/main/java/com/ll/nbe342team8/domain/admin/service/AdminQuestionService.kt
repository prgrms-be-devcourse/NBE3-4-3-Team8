package com.ll.nbe342team8.domain.admin.service

import com.ll.nbe342team8.domain.admin.dto.AdminQuestionDto
import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository
import com.ll.nbe342team8.global.exceptions.ServiceException
import com.ll.nbe342team8.standard.PageDto.PageDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class AdminQuestionService(
    private val questionRepository: QuestionRepository
) {

    fun getQuestionsForAdmin(keyword: String?, hasAnswer: Boolean?, pageable: Pageable): PageDto<AdminQuestionDto> {
        val questionPage = fetchQuestions(keyword, hasAnswer, pageable)
        return PageDto(questionPage.map { AdminQuestionDto(it) })
    }

    // 검색 조건(키워드, 답변 상태)에 맞는 질문 목록을 조회
    private fun fetchQuestions(keyword: String?, hasAnswer: Boolean?, pageable: Pageable): Page<Question> {
        if (isBlank(keyword) && hasAnswer == null) {
            return questionRepository.findAll(pageable)
        }

        if (!isBlank(keyword)) {
            return searchByKeywordAndAnswerStatus(keyword!!.trim(), hasAnswer, pageable)
        }

        return searchByAnswerStatus(hasAnswer!!, pageable)
    }

    // 문자열이 비어있는지 확인
    private fun isBlank(str: String?): Boolean {
        return str == null || str.trim().isEmpty()
    }

    // 키워드 및 답변 상태에 따라 질문 검색
    private fun searchByKeywordAndAnswerStatus(keyword: String, hasAnswer: Boolean?, pageable: Pageable): Page<Question> {
        return when (hasAnswer) {
            null -> questionRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable)
            true -> questionRepository.findByAnswersIsNotEmptyAndTitleContainingOrContentContaining(keyword, keyword, pageable)
            false -> questionRepository.findByAnswersIsEmptyAndTitleContainingOrContentContaining(keyword, keyword, pageable)
        }
    }

    // 답변 상태에 따라 질문 검색
    private fun searchByAnswerStatus(hasAnswer: Boolean, pageable: Pageable): Page<Question> {
        return if (hasAnswer) {
            questionRepository.findByAnswersIsNotEmpty(pageable)
        } else {
            questionRepository.findByAnswersIsEmpty(pageable)
        }
    }

    // ID를 기반으로 특정 질문을 조회
    fun getQuestionById(id: Long): Question {
        return questionRepository.findById(id)
            .orElseThrow { ServiceException(404, "해당 질문을 찾을 수 없습니다.") }
    }

    // 관리자 권한으로 질문 삭제
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteQuestion(id: Long) {
        val question = getQuestionById(id)
        questionRepository.delete(question)
    }
}