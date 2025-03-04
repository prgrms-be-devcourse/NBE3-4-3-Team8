package com.ll.nbe342team8.domain.qna.question.repository

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDtoProjection
import com.ll.nbe342team8.domain.qna.question.entity.Question
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface QuestionRepository : JpaRepository<Question, Long> {
    fun findByTitleContainingOrContentContaining(title: String, content: String, pageable: Pageable): Page<Question>

    fun findByAnswersIsNotEmptyAndTitleContainingOrContentContaining(
        title: String, content: String, pageable: Pageable
    ): Page<Question>

    fun findByAnswersIsEmptyAndTitleContainingOrContentContaining(
        title: String, content: String, pageable: Pageable
    ): Page<Question>

    fun findByAnswersIsNotEmpty(pageable: Pageable): Page<Question>
    fun findByAnswersIsEmpty(pageable: Pageable): Page<Question>

    fun findByMember(pageable: Pageable, member: Member): Page<QuestionListDtoProjection>

    fun existsByMemberAndTitleAndContentAndCreateDateAfter(
        member: Member, title: String, content: String, cutoffTime: LocalDateTime
    ): Boolean
}