package com.ll.nbe342team8.domain.qna.question.service

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDto
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDtoProjection
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto
import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository
import com.ll.nbe342team8.standard.PageDto.PageDto
import org.springframework.transaction.annotation.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@RequiredArgsConstructor
@Service
class QuestionService(
    private val questionRepository: QuestionRepository
) {

    @Transactional
    fun createQuestion(member: Member, dto: ReqQuestionDto): Question {
        // 이스케이프 처리
        val question = Question.create(dto, member)
        return questionRepository.save(question)
    }


    @Transactional(readOnly = true)
    fun getPage(member: Member, page: Int): PageDto<QuestionListDto> {
        val pageable = PageRequest.of(page, 10, Sort.by("createDate").descending())
        val paging: Page<QuestionListDtoProjection> = questionRepository.findByMember(pageable, member)

        val pagingOrderDto: Page<QuestionListDto> = paging.map { QuestionListDto(it) }
        return PageDto(pagingOrderDto)
    }

    @Transactional(readOnly = true)
    fun getNextOrBeforePage(member: Member, lastQuestionId: Long?, firstQuestionId: Long?): PageDto<QuestionListDto> {
        val pageSize = 10 // 페이지 크기
        val pageable = PageRequest.of(0, pageSize) // 첫 페이지, pageSize만큼의 결과

        // Keyset Pagination 적용
        val questionList: List<QuestionListDtoProjection> = if (lastQuestionId != null) {
            questionRepository.findByMemberWithKeysetNext(member, lastQuestionId, pageable)
        } else {
            questionRepository.findByMemberWithKeysetPrev(member, firstQuestionId!!, pageable)
        }

        val questionDtoList = questionList.map { QuestionListDto(it) }

        return PageDto(questionDtoList)
    }



    @Transactional
    fun deleteQuestion(question: Question) {
        questionRepository.delete(question)
    }

    @Transactional
    fun modifyQuestion(question: Question, dto: ReqQuestionDto?) {
        question.updateQuestionInfo(dto)
    }

    @Transactional
    fun findById(id: Long): Optional<Question> {
        return questionRepository.findById(id)
    }

    //수정, 삭제하려는 게시글을 사용자가 작성한지 학인
    fun isQuestionOwner(member: Member, question: Question): Boolean {
        return question.member.id == member.id
    }

    //네트워크 지연, 스팸 봇, 답변 등록 버튼 연타로 생성되는 중복 답변 방지
    fun existsDuplicateQuestionInShortTime(
        member: Member,
        title: String,
        content: String,
        duration: Duration
    ): Boolean {
        val cutoffTime = LocalDateTime.now().minus(duration)
        return questionRepository.existsByMemberAndTitleAndContentAndCreateDateAfter(
            member,
            title,
            content,
            cutoffTime
        )
    }

    fun flush() {
        questionRepository.flush() // em.flush(); 와 동일
    }
}