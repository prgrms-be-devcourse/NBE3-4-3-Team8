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
import java.time.Instant
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


    fun findByTitle(title: String): Optional<Question>

    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.genFiles WHERE q.id = :id")
    fun findByIdWithGenFiles(@Param("id") id: Long?): Optional<Question?>?

    @Query("SELECT q FROM Question q JOIN FETCH q.member WHERE q.id = :id")
    fun findByIdWithMember(@Param("id") id: Long): Optional<Question>

    // 이전 페이지 (더 오래된 질문 목록 조회)
    @Query("SELECT q FROM Question q WHERE q.member = :member AND q.createDate < :before ORDER BY q.createDate DESC")
    fun findByMemberAndCreateDateBefore(
        @Param("member") member: Member,
        @Param("before") before: LocalDateTime,
        @Param("size") size: Int = 10
    ): List<Question>

    // 이후 페이지 (더 최신 질문 목록 조회)
    @Query("SELECT q FROM Question q WHERE q.member = :member AND q.createDate > :after ORDER BY q.createDate ASC")
    fun findByMemberAndCreateDateAfter(
        @Param("member") member: Member,
        @Param("after") after: LocalDateTime,
        @Param("size") size: Int = 10
    ): List<Question>

    // 최신 데이터 조회 (초기 페이지)
    @Query("SELECT q FROM Question q WHERE q.member = :member ORDER BY q.createDate DESC")
    fun findLatestByMember(
        @Param("member") member: Member,
        @Param("size") size: Int = 10
    ): List<Question>







}