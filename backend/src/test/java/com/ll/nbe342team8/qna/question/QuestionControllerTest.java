package com.ll.nbe342team8.qna.question;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe342team8.domain.member.deliveryInformation.controller.DeliveryInformationController;
import com.ll.nbe342team8.domain.member.deliveryInformation.dto.ReqDeliveryInformationDto;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.deliveryInformation.repository.DeliveryInformationRepository;
import com.ll.nbe342team8.domain.member.deliveryInformation.service.DeliveryInformationService;
import com.ll.nbe342team8.domain.member.member.controller.MemberController;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.qna.answer.dto.ReqAnswerDto;
import com.ll.nbe342team8.domain.qna.answer.service.AnswerService;
import com.ll.nbe342team8.domain.qna.question.controller.QuestionController;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Member mockMember;

    @Autowired
    MemberService memberService;

    @Autowired
    QuestionRepository questionRepository;

    @BeforeEach
    void setup() {
        // ✅ Mock Member 생성
        mockMember = new Member();
        mockMember.setOAuthId("31313");
        mockMember.setPhoneNumber("010-1111-2222");
        mockMember.setName("테스트 유저");

        Question question1 = Question.builder()
                                .title("제목1")
                                .content("내용1")
                                .member(mockMember)
                                .build();

        Question question2 = Question.builder()
                .title("제목2")
                .content("내용2")
                .member(mockMember)
                .build();

        //Mock Security Context (인증된 사용자 정보 설정)
        mockMember.setQuestions(new ArrayList<>(List.of(question1, question2)));

        memberService.saveMember(mockMember);

        for (Question question : mockMember.getQuestions()) {
            questionRepository.save(question);
        }

        // Security Context에 인증 정보 추가
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new SecurityUser(mockMember), // ✅ SecurityUser를 사용해서 인증
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);



    }

    /*
    @Test
    @DisplayName("사용자 질문 불러오기1")
    void getMyQuestions1() throws Exception {

        // ✅ 3. API 요청
        ResultActions resultActions = mockMvc.perform(
                        get("/my/question")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // ✅ 4. 응답 검증
        resultActions
                .andExpect(handler().handlerType(QuestionController.class))
                .andExpect(handler().methodName("getQuestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].title").value("제목2"))
                .andExpect(jsonPath("$.items[0].content").value("내용2"))
                .andExpect(jsonPath("$.items[1].title").value("제목1"))
                .andExpect(jsonPath("$.items[1].content").value("내용1"));
    }

     */

    @Test
    @DisplayName("keyset을 적용한 사용자 질문 불러오기")
    void getMyKeysetQuestions2() throws Exception {

        // ✅ 3. API 요청
        ResultActions resultActions = mockMvc.perform(
                        get("/my/question?page=0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // ✅ 4. 응답 검증
        resultActions
                .andExpect(handler().handlerType(QuestionController.class))
                .andExpect(handler().methodName("getKeySetQuestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].title").value("제목2"))
                .andExpect(jsonPath("$.items[0].content").value("내용2"))
                .andExpect(jsonPath("$.items[1].title").value("제목1"))
                .andExpect(jsonPath("$.items[1].content").value("내용1"));
    }

    @Test
    @DisplayName("사용자 상세 질문 불러오기1")
    void getMyQuestion1() throws Exception {

        Long id=1L;
        ResultActions resultActions = mockMvc.perform(
                        get("/my/question/{id}",id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // ✅ 4. 응답 검증
        resultActions
                .andExpect(handler().handlerType(QuestionController.class))
                .andExpect(handler().methodName("getQuestion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목1"))
                .andExpect(jsonPath("$.content").value("내용1"));

    }


    @Test
    @DisplayName("질문 추가1")
    void postQuestionTest1() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        ReqQuestionDto reqQuestionDto=new ReqQuestionDto("제목3","내용3");
        String requestBody =objectMapper.writeValueAsString(reqQuestionDto);

        ResultActions resultActions = mockMvc.perform(
                        post("/my/question")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(requestBody)
                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(QuestionController.class))
                .andExpect(handler().methodName("postQuestion"))
                .andExpect(status().isCreated());

        Optional<Question> savedQuestion = questionRepository.findByTitle("제목3");

        assertTrue(savedQuestion.isPresent(), "데이터베이스에 저장된 질문이 존재해야 합니다.");
        assertEquals("제목이 일치해야합니다","제목3" , savedQuestion.get().getTitle());
        assertEquals("내용이 일치해야합니다", "내용3", savedQuestion.get().getContent());

    }


    @Test
    @DisplayName("사용자 질문 수정1")
    void putQuestionTest1() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        ReqQuestionDto reqQuestionDto=new ReqQuestionDto("수정된 제목","수정된 내용");
        String requestBody =objectMapper.writeValueAsString(reqQuestionDto);

        Long id=1L;
        ResultActions resultActions = mockMvc.perform(
                        put("/my/question/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(requestBody)

                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(QuestionController.class))
                .andExpect(handler().methodName("putQuestion"))
                .andExpect(status().isOk());

        Optional<Question> modifyedQuestion = questionRepository.findByTitle("수정된 제목");

        assertEquals("제목이 일치해야합니다","수정된 제목" , modifyedQuestion.get().getTitle());
        assertEquals("내용이 일치해야합니다", "수정된 내용", modifyedQuestion.get().getContent());


    }

    @Test
    @DisplayName("사용자 질문 삭제1")
    void deleteQuestionTest1() throws Exception {

        Long id =mockMember.getQuestions().getFirst().getId();

        ResultActions resultActions = mockMvc.perform(
                        delete("/my/question/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)

                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(QuestionController.class))
                .andExpect(handler().methodName("deleteQuestion"))
                .andExpect(status().isNoContent());

        Optional<Question> deletedQuestion = questionRepository.findById(id);

        assertTrue(deletedQuestion.isEmpty(), "데이터 삭제에 실패했습니다.");

    }


}
