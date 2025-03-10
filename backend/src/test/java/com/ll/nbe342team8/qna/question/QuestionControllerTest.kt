package com.ll.nbe342team8.qna.question

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.service.MemberService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.domain.qna.question.controller.QuestionController
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto
import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository

import java.nio.charset.StandardCharsets
import java.util.List
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.hasSize
import org.hibernate.validator.internal.util.Contracts.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class QuestionControllerTest @Autowired constructor (
    private val mockMvc: MockMvc,
    private val memberService: MemberService,
    private val questionRepository: QuestionRepository
    ) {

    private lateinit var mockMember: Member


    @BeforeEach
    fun setup() {
        // ✅ Mock Member 생성
        mockMember = Member()
        mockMember.setOAuthId("31313")
        mockMember.setPhoneNumber("010-1111-2222")
        mockMember.setName("테스트 유저")

        val question1 = Question.builder()
            .title("제목1")
            .content("내용1")
            .isAnswer(false)
            .build()

        val question2 = Question.builder()
            .title("제목2")
            .content("내용2")
            .isAnswer(false)
            .build()

        mockMember.addQuestion(question1)  // 연관 관계 편의 메서드 사용
        mockMember.addQuestion(question2)

        memberService.saveMember(mockMember)  // member를 저장하면 question도 저장됨

        // Security Context에 인증 정보 추가
        val securityContext = SecurityContextHolder.createEmptyContext()
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            SecurityUser(mockMember),  //  SecurityUser를 사용해서 인증
            null,
            List.of(SimpleGrantedAuthority("ROLE_USER"))
        )
        securityContext.authentication = authentication
        SecurityContextHolder.setContext(securityContext)
    }


    @Test
    @DisplayName("사용자 질문 불러오기1")
    fun getMyQuestions1() {
        // ✅ 3. API 요청
        val resultActions = mockMvc.perform(
            get("/my/question")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        )
            .andDo(print())

        // ✅ 4. 응답 검증
        resultActions
            .andExpect(handler().handlerType(QuestionController::class.java))
            .andExpect(handler().methodName("getQuestions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalItems").value(2))
            .andExpect(jsonPath("$.items", hasSize<Any>(2))) // 타입 명시적으로 지정
            .andExpect(jsonPath("$.items[0].title").value("제목2"))
            .andExpect(jsonPath("$.items[0].content").value("내용2"))
            .andExpect(jsonPath("$.items[1].title").value("제목1"))
            .andExpect(jsonPath("$.items[1].content").value("내용1"))
    }


    /*
    @Test
    @DisplayName("keyset을 적용한 사용자 질문 불러오기")
    fun getMyKeysetQuestions2() {
        // ✅ 3. API 요청
        val resultActions = mockMvc.perform(
            get("/my/question?page=0")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        )
            .andDo(print())

        // ✅ 4. 응답 검증
        resultActions
            .andExpect(handler().handlerType(QuestionController::class.java))
            .andExpect(handler().methodName("getKeySetQuestions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalItems").value(2))
            .andExpect(jsonPath("$.items", hasSize<Any>(2))) // 타입 명시적으로 지정
            .andExpect(jsonPath("$.items[0].title").value("제목2"))
            .andExpect(jsonPath("$.items[0].content").value("내용2"))
            .andExpect(jsonPath("$.items[1].title").value("제목1"))
            .andExpect(jsonPath("$.items[1].content").value("내용1"))
    }
    */

    @Test
    @DisplayName("사용자 상세 질문 불러오기1")
    fun getMyQuestion1() {
        val id: Long = 1L

        val resultActions = mockMvc.perform(
            get("/my/question/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        )
            .andDo(print())

        // ✅ 4. 응답 검증
        resultActions
            .andExpect(handler().handlerType(QuestionController::class.java))
            .andExpect(handler().methodName("getQuestion"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("제목1"))
            .andExpect(jsonPath("$.content").value("내용1"))
    }



    @Test
    @DisplayName("질문 추가1")
    fun postQuestionTest1() {
        val objectMapper = ObjectMapper()

        val reqQuestionDto = ReqQuestionDto("제목3", "내용3")
        val requestBody = objectMapper.writeValueAsString(reqQuestionDto)

        val resultActions = mockMvc.perform(
            post("/my/question")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(requestBody)
        )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(QuestionController::class.java))
            .andExpect(handler().methodName("postQuestion"))
            .andExpect(status().isCreated)

        val savedQuestion = questionRepository.findByTitle("제목3")

        assertTrue(savedQuestion.isPresent, "데이터베이스에 저장된 질문이 존재해야 합니다.")
        assertEquals("제목3", savedQuestion.get().title,"제목이 일치해야합니다.")
        assertEquals("내용3", savedQuestion.get().content, "내용이 일치해야합니다.")
    }



    @Test
    @DisplayName("사용자 질문 수정1")
    fun putQuestionTest1() {
        val objectMapper = ObjectMapper()

        val reqQuestionDto = ReqQuestionDto("수정된 제목", "수정된 내용")
        val requestBody = objectMapper.writeValueAsString(reqQuestionDto)

        val id: Long = 1L
        val resultActions = mockMvc.perform(
            put("/my/question/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(requestBody)
        )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(QuestionController::class.java))
            .andExpect(handler().methodName("putQuestion"))
            .andExpect(status().isOk)

        val modifiedQuestion = questionRepository.findByTitle("수정된 제목")

        assertEquals("수정된 제목", modifiedQuestion.get().title, "제목이 일치해야합니다")
        assertEquals("수정된 내용", modifiedQuestion.get().content, "내용이 일치해야합니다.")
    }

    @Test
    @DisplayName("사용자 질문 삭제1")
    fun deleteQuestionTest1() {
        val id = mockMember.questions.first().id

        val resultActions = mockMvc.perform(
            delete("/my/question/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(QuestionController::class.java))
            .andExpect(handler().methodName("deleteQuestion"))
            .andExpect(status().isNoContent)

        val deletedQuestion = questionRepository.findById(id)

        assertTrue(deletedQuestion.isEmpty, "데이터 삭제에 실패했습니다.")
    }

}
