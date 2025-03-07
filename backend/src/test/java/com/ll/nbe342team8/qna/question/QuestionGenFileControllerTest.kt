package com.ll.nbe342team8.qna.question

import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.domain.member.member.service.MemberService
import com.ll.nbe342team8.domain.oauth.SecurityUser
import com.ll.nbe342team8.domain.qna.question.controller.QuestionGenFileController
import com.ll.nbe342team8.domain.qna.question.entity.Question
import com.ll.nbe342team8.domain.qna.question.repository.QuestionGenFileRepository
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository

import java.io.File
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.util.List
import jakarta.servlet.http.HttpServletRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print


@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class QuestionGenFileControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val memberService: MemberService,
    private val questionRepository: QuestionRepository,
    private val questionGenFileRepository: QuestionGenFileRepository,
    private val request: HttpServletRequest
    ) {

    private lateinit var mockMember: Member

    // 해당 경로에 테스트용 업로드 이미지 데이터가 필요함
    var filePath: String = "c:/temp/glog_dev/postGenFileTest/testImage.png"

    @BeforeEach
    fun setup() {
        // ✅ Mock Member 생성
        mockMember = Member()
        mockMember.setOAuthId("31313")
        mockMember.setPhoneNumber("010-1111-2222")
        mockMember.setName("테스트 유저")

        var question = Question.builder()
            .title("제목1")
            .content("내용1")
            .member(mockMember)
            .build()

        //Mock Security Context (인증된 사용자 정보 설정)
        mockMember.setQuestions(ArrayList(List.of(question)))

        memberService.saveMember(mockMember)

        questionRepository.save(question)

        question = questionRepository.findByIdWithGenFiles(question.getId())!!.orElseThrow()!!

        val postGenFile = question.addGenFile("attachment", filePath)

        questionGenFileRepository.save(postGenFile)

        // Security Context에 인증 정보 추가
        val securityContext = SecurityContextHolder.createEmptyContext()
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            SecurityUser(mockMember),  // ✅ SecurityUser를 사용해서 인증
            null,
            List.of(SimpleGrantedAuthority("ROLE_USER"))
        )
        securityContext.authentication = authentication
        SecurityContextHolder.setContext(securityContext)
    }

    @Test
    @DisplayName("사용자 이미지 불러오기")
    fun getImages() {
        val question = questionRepository.findByIdWithGenFiles(mockMember.questions.first().id)?.orElseThrow()
        val questionGenFile = question!!.genFiles.first()

        // ✅ 3. API 요청
        val resultActions = mockMvc.perform(
            get("/my/question/genFile/download/{questionId}/{fileNo}", question.id, questionGenFile.fileNo)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        )


        // ✅ 4. 응답 검증
        resultActions
            .andExpect(handler().handlerType(QuestionGenFileController::class.java))
            .andExpect(handler().methodName("loadImage"))
            .andExpect(status().isOk)
            .andExpect { result ->
                val filePath = questionGenFile.filePath // filePath 변수가 필요합니다.
                val expectedContentType = request.getServletContext().getMimeType(filePath)
                val finalContentType = expectedContentType ?: "application/octet-stream"
                assertThat(result.response.contentType).isEqualTo(finalContentType)
            }
    }


    @Test
    @DisplayName("사용자 이미지 추가하기")
    fun postImages() {
        val question = questionRepository.findByIdWithGenFiles(mockMember.questions.first().id)?.orElseThrow()
        val file = File(filePath)

        val multipartFile = MockMultipartFile(
            "file", // 컨트롤러의 @RequestParam("file")과 동일한 이름
            file.name,
            "image/png", // MIME 타입 설정
            FileInputStream(file).readBytes() // 파일 읽기
        )

        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.multipart("/my/question/genFile/{questionId}/attachment", question!!.id)
                .file(multipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .characterEncoding(StandardCharsets.UTF_8)
        )
            .andDo(print())

        // ✅ 4. 응답 검증
        resultActions
            .andExpect(handler().handlerType(QuestionGenFileController::class.java))
            .andExpect(handler().methodName("makeNewFile"))
            .andExpect(status().isCreated)

        val savedQuestion = questionRepository.findByIdWithGenFiles(mockMember.questions.first().id)?.orElseThrow()
        val savedQuestionGenFiles = savedQuestion?.genFiles

        assertEquals(2, savedQuestionGenFiles?.size, "데이터베이스에 저장된 질문이 존재해야 합니다.")
    }


    @Test
    @DisplayName("사용자 이미지 삭제하기")
    fun deleteImages() {
        val question = questionRepository.findByIdWithGenFiles(mockMember.questions.first().id)?.orElseThrow()
        val questionGenFile = question?.genFiles?.first()

        // ✅ 3. API 요청
        val resultActions = mockMvc.perform(
            delete("/my/question/genFile/{questionId}/{fileNo}/attachment", question!!.id, questionGenFile?.fileNo)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        )
            .andDo(print())

        // ✅ 4. 응답 검증
        resultActions
            .andExpect(handler().handlerType(QuestionGenFileController::class.java))
            .andExpect(handler().methodName("deleteImageFile"))
            .andExpect(status().isNoContent)
    }

}
