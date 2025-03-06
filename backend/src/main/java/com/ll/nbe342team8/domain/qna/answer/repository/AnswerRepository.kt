package com.ll.nbe342team8.domain.qna.answer.repository

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.qna.answer.entity.Answer
import com.ll.nbe342team8.domain.qna.question.entity.Question
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AnswerRepository : JpaRepository<Answer, Long> {
    fun existsByQuestionAndMemberAndContentAndCreateDateAfter(
        question: Question,
        member: Member,
        content: String,
        cutoffTime: LocalDateTime
    ): Boolean

    fun findByQuestionOrderByCreateDateDesc(question: Question): List<Answer>

    fun findByQuestionId(questionId: Long): List<Answer>
}
