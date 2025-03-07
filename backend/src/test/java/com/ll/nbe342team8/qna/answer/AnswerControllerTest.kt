package com.ll.nbe342team8.qna.answer

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.service.MemberService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.domain.qna.answer.controller.AnswerController
import com.ll.nbe342team8.domain.qna.answer.dto.ReqAnswerDto
import com.ll.nbe342team8.domain.qna.answer.repository.AnswerRepository
import com.ll.nbe342team8.domain.qna.answer.service.AnswerService
import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler
import java.nio.charset.StandardCharsets
import java.util.List
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print


@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class AnswerControllerTest @Autowired constructor (
    private val mockMvc: MockMvc,
    private val memberService: MemberService,
    private val answerService: AnswerService,
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository
    ) {

    private lateinit var mockMember: Member

    @BeforeEach
    fun setup() {
        // ✅ Mock Member 생성
        mockMember = Member()
        mockMember.setOAuthId("31313")
        mockMember.setPhoneNumber("010-1111-2222")
        mockMember.setName("관리자 테스트 유저")
        mockMember.setMemberType(Member.MemberType.ADMIN)

        val question1 = Question.builder()
            .title("제목1")
            .content("내용1")
            .member(mockMember)
            .answers(ArrayList())
            .build()

        val question2 = Question.builder()
            .title("제목2")
            .content("내용2")
            .member(mockMember)
            .answers(ArrayList())
            .build()

        //Mock Security Context (인증된 사용자 정보 설정)
        mockMember.setQuestions(ArrayList(List.of(question1, question2)))

        memberService.saveMember(mockMember)

        for (question in mockMember.getQuestions()) {
            questionRepository.save(question)
        }

        val reqAnswerDto1 = ReqAnswerDto("answer1")


        answerService.createAnswer(question1, mockMember, reqAnswerDto1)


        // Security Context에 인증 정보 추가
        val securityContext = SecurityContextHolder.createEmptyContext()
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            SecurityUser(mockMember),  // ✅ SecurityUser를 사용해서 인증
            null,
            List.of(SimpleGrantedAuthority("RULE_ADMIN"))
        )
        securityContext.authentication = authentication
        SecurityContextHolder.setContext(securityContext)
    }


    @Test
    @DisplayName("관리자 답변 불러오기1")
    fun getAnswers1() {
        val question1 = mockMember?.questions?.first()
        val question2 = mockMember?.questions?.get(1)
        val answer1 = question1?.answers?.first()

        val resultActions = mockMvc.perform(
            get("/admin/dashboard/questions/{questionId}/answers", question1?.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(AnswerController::class.java))
            .andExpect(handler().methodName("getAnswers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.answers", hasSize<Any>(1)))
            .andExpect(jsonPath("$.answers[0].content").value(answer1?.content))

        val resultActions2 = mockMvc.perform(
            get("/admin/dashboard/questions/{questionId}/answers", question2?.id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        )
            .andDo(print())

        resultActions2
            .andExpect(handler().handlerType(AnswerController::class.java))
            .andExpect(handler().methodName("getAnswers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.answers", hasSize<Any>(0)))
    }

    @Test
    @DisplayName("관리자 답변 상세 내역 불러오기1")
    fun getAnswer1() {
        val question = mockMember.questions?.first()
        val answer = question?.answers?.first()

        val resultActions = mockMvc.perform(
            get("/admin/dashboard/questions/{questionId}/answers/{answerId}", question?.id, answer?.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(AnswerController::class.java))
            .andExpect(handler().methodName("getAnswer"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value(answer?.content))
    }



    @Test
    @DisplayName("관리자 답변 추가1")
    fun postAnswerTest1() {
        val question = mockMember.questions[1]
        val objectMapper = ObjectMapper()

        val reqAnswerDto = ReqAnswerDto("새로 추가된 답변 내용")
        val requestBody = objectMapper.writeValueAsString(reqAnswerDto)

        val resultActions = mockMvc.perform(
            post("/admin/dashboard/questions/{questionId}/answers", question.id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(requestBody)
        )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(AnswerController::class.java))
            .andExpect(handler().methodName("postAnswer"))
            .andExpect(status().isCreated)

        val savedAnswer = answerRepository.findByContent("새로 추가된 답변 내용")

        assertTrue(savedAnswer.isPresent, "데이터베이스에 저장된 질문이 존재해야 합니다.")
        assertEquals("새로 추가된 답변 내용", savedAnswer.get().content, "내용이 일치해야합니다.")
    }



    @Test
    @DisplayName("관리자 답변 수정1")
    fun putQuestionTest1() {
        val question = mockMember.questions.first()
        val answer = question.answers.first()

        val objectMapper = ObjectMapper()

        val reqAnswerDto = ReqAnswerDto("수정된 답변 내용")
        val requestBody = objectMapper.writeValueAsString(reqAnswerDto)

        val resultActions = mockMvc.perform(
            put("/admin/dashboard/questions/{questionId}/answers/{answerId}", question.id, answer.id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(requestBody)
        )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(AnswerController::class.java))
            .andExpect(handler().methodName("modifyAnswer"))
            .andExpect(status().isOk)

        val savedAnswer = answerRepository.findByContent("수정된 답변 내용")

        assertTrue(savedAnswer.isPresent, "데이터베이스에 저장된 질문이 존재해야 합니다.")
        assertEquals("수정된 답변 내용", savedAnswer.get().content, "내용이 일치해야합니다.")
    }


    @Test
    @DisplayName("사용자 질문 삭제1")
    fun deleteAnswerTest1() {
        val question = mockMember.questions.first()
        val answer = question.answers.first()

        val resultActions = mockMvc.perform(
            delete("/admin/dashboard/questions/{questionId}/answers/{answerId}", question.id, answer.id)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        )
            .andDo(print())

        resultActions
            .andExpect(handler().handlerType(AnswerController::class.java))
            .andExpect(handler().methodName("deleteAnswer"))
            .andExpect(status().isNoContent)

        val savedAnswer = answerRepository.findById(answer.id)

        assertTrue(savedAnswer.isEmpty, "데이터가 삭제되지 않았습니다.")
    }

}
