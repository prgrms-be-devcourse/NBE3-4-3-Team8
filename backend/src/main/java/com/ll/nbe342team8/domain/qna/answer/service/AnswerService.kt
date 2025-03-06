package com.ll.nbe342team8.domain.qna.answer.service

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.qna.answer.dto.ReqAnswerDto
import com.ll.nbe342team8.domain.qna.answer.entity.Answer
import com.ll.nbe342team8.domain.qna.answer.repository.AnswerRepository
import com.ll.nbe342team8.domain.qna.question.entity.Question
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@RequiredArgsConstructor
@Service
class AnswerService (
    private val answerRepository: AnswerRepository
    ) {

    @Transactional
    fun createAnswer(question: Question, member: Member, dto: ReqAnswerDto) {

        val answer = Answer.from(dto.content, member, question)
        question.addAnswer(answer)
        question.isAnswer = true
        answerRepository.save(answer)
    }

    @Transactional
    fun modifyAnswer(answer: Answer, dto: ReqAnswerDto) {
        // 이스케이프 처리한 데이터를 question 객체에 갱신
        answer.updateAnswerInfo(dto)
    }

    @Transactional
    fun deleteAnswer(answer: Answer) {
        val question = answer.question
        question.removeAnswer(answer)
        answerRepository.delete(answer)
    }

    fun findById(id: Long): Optional<Answer> {
        return answerRepository.findById(id)
    }

    fun findByQuestion(question: Question): List<Answer> {
        return answerRepository.findByQuestionOrderByCreateDateDesc(question)
    }

    // 네트워크 지연, 스팸 봇, 답변 등록 버튼 연타로 생성되는 중복 답변 방지
    fun existsDuplicateAnswerInShortTime(question: Question, member: Member, content: String, duration: Duration): Boolean {
        val cutoffTime = LocalDateTime.now().minus(duration)
        return answerRepository.existsByQuestionAndMemberAndContentAndCreateDateAfter(question, member, content, cutoffTime)
    }

}