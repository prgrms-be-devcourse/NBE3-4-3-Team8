package com.ll.nbe342team8.domain.qna.question.repository

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDtoProjection
import com.ll.nbe342team8.domain.qna.question.entity.Question
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

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


    // 다음 페이지 조회 (현재 페이지 마지막 ID보다 작은 데이터)
    @Query("SELECT q FROM Question q WHERE q.member = :member AND q.id < :lastQuestionId ORDER BY q.createDate DESC")
    fun findByMemberWithKeysetNext(
        @Param("member") member: Member,
        @Param("lastQuestionId") lastQuestionId: Long,
        pageable: Pageable
    ): List<QuestionListDtoProjection>

    // 이전 페이지 조회 (현재 페이지 첫 번째 ID보다 큰 데이터)
    @Query("SELECT q FROM Question q WHERE q.member = :member AND q.id > :firstQuestionId ORDER BY q.createDate ASC")
    fun findByMemberWithKeysetPrev(
        @Param("member") member: Member,
        @Param("firstQuestionId") firstQuestionId: Long,
        pageable: Pageable
    ): List<QuestionListDtoProjection>

    fun findByTitle(title: String): Optional<Question>

    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.genFiles WHERE q.id = :id")
    fun findByIdWithGenFiles(@Param("id") id: Long?): Optional<Question?>?





}