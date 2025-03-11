package com.ll.nbe342team8.domain.qna.question.service

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.qna.question.dto.CursorPageDto
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDto
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDtoProjection
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto
import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository
import com.ll.nbe342team8.global.exceptions.ServiceException
import com.ll.nbe342team8.standard.PageDto.PageDto
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.transaction.annotation.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@RequiredArgsConstructor
@Service
class QuestionService(
    private val questionRepository: QuestionRepository
) {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional
    fun createQuestion(member: Member, dto: ReqQuestionDto): Question {
        // 이스케이프 처리
        val question = Question.create(dto, member)
        return questionRepository.save(question)
    }


    @Transactional(readOnly = true)
    fun getPage(member: Member, page: Int, size: Int): PageDto<QuestionListDto> {
        val pageable = PageRequest.of(page, size, Sort.by("createDate").descending())
        val paging: Page<QuestionListDtoProjection> = questionRepository.findByMember(pageable, member)

        val pagingOrderDto: Page<QuestionListDto> = paging.map { QuestionListDto(it) }
        return PageDto(pagingOrderDto)
    }

    @Transactional(readOnly = true)
    fun getCursorPage(member: Member, before: LocalDateTime?, after: LocalDateTime?,size:Int): CursorPageDto<QuestionListDto> {
        val questions: List<Question>
        val nextCursor: LocalDateTime?
        val prevCursor: LocalDateTime?

        when {
            before != null -> { // 이전 페이지 (더 오래된 데이터)
                //questions = questionRepository.findByMemberAndCreateDateBefore(member, before, size)
                val query = entityManager.createQuery("SELECT q FROM Question q WHERE q.member = :member AND q.createDate < :before ORDER BY q.createDate DESC", Question::class.java)
                query.setParameter("member", member)
                query.setParameter("before", before)
                query.maxResults = 10  // 10개의 항목만 가져옴
                questions = query.resultList
                nextCursor = questions.minByOrNull { it.createDate }?.createDate
                prevCursor = questions.maxByOrNull { it.createDate }?.createDate
            }

            after != null -> { // 이후 페이지 (더 최신 데이터)
                //questions = questionRepository.findByMemberAndCreateDateAfter(member, after, size)
                val query = entityManager.createQuery("SELECT q FROM Question q WHERE q.member = :member AND q.createDate > :after ORDER BY q.createDate ASC", Question::class.java)
                query.setParameter("member", member)
                query.setParameter("after", after)
                query.maxResults = 10  // 10개의 항목만 가져옴
                questions = query.resultList
                nextCursor = questions.minByOrNull { it.createDate }?.createDate
                prevCursor = questions.maxByOrNull { it.createDate }?.createDate
            }

            else -> { // 기본적으로 최신 데이터 조회
                questions = questionRepository.findLatestByMember(member, size)
                nextCursor = questions.minByOrNull { it.createDate }?.createDate
                prevCursor = questions.maxByOrNull { it.createDate }?.createDate
            }
        }

        // DTO 변환 및 반환
        return CursorPageDto(
            items = questions.map { QuestionListDto(it) },
            nextCursor = nextCursor,
            prevCursor = prevCursor
        )
    }



    @Transactional
    fun deleteQuestion(question: Question) {

        // 사진 저장 디렉토리 내부 사진 데이터 삭제
        val genFilesToDelete = question.genFiles.toList()
        for(genFile in genFilesToDelete) {
            question.deleteGenFile(genFile.typeCode, genFile.fileNo)
        }

        questionRepository.delete(question)
    }

    @Transactional
    fun modifyQuestion(question: Question, dto: ReqQuestionDto?) {
        question.updateQuestionInfo(dto)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Question {
        return questionRepository.findByIdWithMember(id)
            .orElseThrow { ServiceException(HttpStatus.NOT_FOUND.value(), "질문을 찾을 수 없습니다.") }
    }

    //사용자 권한 확인, 관리자 계정이여도 접근 가능
    fun validateQuestionOwner(member: Member, question: Question) {
        require(isQuestionOwner(member, question) || member.checkAdmin()) {
            throw ServiceException(HttpStatus.UNAUTHORIZED.value(), "권한이 없습니다.")
        }
    }



    fun validateExistsDuplicateQuestionInShortTime(member: Member, title: String, content: String, duration: Duration) {
        require(!existsDuplicateQuestionInShortTime(member, title, content, duration)) {
            throw ServiceException(HttpStatus.TOO_MANY_REQUESTS.value(), "너무 빠르게 동일한 답변을 등록할 수 없습니다.")
        }
    }

    //수정, 삭제하려는 게시글을 사용자가 작성한지 학인
    private fun isQuestionOwner(member: Member, question: Question): Boolean {
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

    fun checkActorCanMakeNewGenFile(member: Member?, question: Question) {
        requireNotNull(member) { "로그인 후 이용해주세요." }
            .takeIf { it.id == question.member.id }
            ?: throw ServiceException(404, "게시글 작성 유저만 업로드 할 수 있습니다.")

        require(question.genFiles.size < 5) { "질문 하나에 이미지는 5개까지 설정할 수 있습니다." }
    }

}