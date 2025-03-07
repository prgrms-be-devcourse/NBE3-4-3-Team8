package com.ll.nbe342team8.qna.answer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.qna.answer.controller.AnswerController;
import com.ll.nbe342team8.domain.qna.answer.dto.AnswerDto;
import com.ll.nbe342team8.domain.qna.answer.dto.GetResAnswersDto;
import com.ll.nbe342team8.domain.qna.answer.dto.ReqAnswerDto;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.answer.repository.AnswerRepository;
import com.ll.nbe342team8.domain.qna.answer.service.AnswerService;
import com.ll.nbe342team8.domain.qna.question.controller.QuestionController;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Member mockMember;

    @Autowired
    MemberService memberService;

    @Autowired
    AnswerService answerService;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @BeforeEach
    void setup() {
        // ✅ Mock Member 생성
        mockMember = new Member();
        mockMember.setOAuthId("31313");
        mockMember.setPhoneNumber("010-1111-2222");
        mockMember.setName("관리자 테스트 유저");
        mockMember.setMemberType(Member.MemberType.ADMIN);

        Question question1 = Question.builder()
                .title("제목1")
                .content("내용1")
                .member(mockMember)
                .answers(new ArrayList<>())
                .build();

        Question question2 = Question.builder()
                .title("제목2")
                .content("내용2")
                .member(mockMember)
                .answers(new ArrayList<>())
                .build();

        //Mock Security Context (인증된 사용자 정보 설정)
        mockMember.setQuestions(new ArrayList<>(List.of(question1, question2)));

        memberService.saveMember(mockMember);

        for (Question question : mockMember.getQuestions()) {
            questionRepository.save(question);
        }

        ReqAnswerDto reqAnswerDto1=new ReqAnswerDto("answer1");


        answerService.createAnswer(question1,mockMember,reqAnswerDto1);


        // Security Context에 인증 정보 추가
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new SecurityUser(mockMember), // ✅ SecurityUser를 사용해서 인증
                null,
                List.of(new SimpleGrantedAuthority("RULE_ADMIN"))
        );
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

    }


    @Test
    @DisplayName("관리자 답변 불러오기1")
    void getAnswers1() throws Exception {
        Question question1=mockMember.getQuestions().getFirst();
        Question question2=mockMember.getQuestions().get(1);
        Answer answer1 =question1.getAnswers().getFirst();

        ResultActions resultActions = mockMvc.perform(
                        get("/admin/dashboard/questions/{questionId}/answers",question1.id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AnswerController.class))
                .andExpect(handler().methodName("getAnswers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answers", hasSize(1)))
                .andExpect(jsonPath("$.answers[0].content").value(answer1.content));

        resultActions = mockMvc.perform(
                        get("/admin/dashboard/questions/{questionId}/answers",question2.id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AnswerController.class))
                .andExpect(handler().methodName("getAnswers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answers", hasSize(0)));




    }

    @Test
    @DisplayName("관리자 답변 상세 내역 불러오기1")
    void getAnswer1() throws Exception {
        Question question=mockMember.getQuestions().getFirst();
        Answer answer =question.getAnswers().getFirst();


        ResultActions resultActions = mockMvc.perform(
                        get("/admin/dashboard/questions/{questionId}/answers/{answerId}",question.id,answer.id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AnswerController.class))
                .andExpect(handler().methodName("getAnswer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(answer.content));





    }


    @Test
    @DisplayName("관리자 답변 추가1")
    void postAnswerTest1() throws Exception {

        Question question=mockMember.getQuestions().get(1);
        ObjectMapper objectMapper = new ObjectMapper();

        ReqAnswerDto reqAnswerDto=new ReqAnswerDto("새로 추가된 답변 내용");
        String requestBody =objectMapper.writeValueAsString(reqAnswerDto);

        ResultActions resultActions = mockMvc.perform(
                        post("/admin/dashboard/questions/{questionId}/answers",question.id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(requestBody)
                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(AnswerController.class))
                .andExpect(handler().methodName("postAnswer"))
                .andExpect(status().isCreated());

        Optional<Answer> savedAnswer = answerRepository.findByContent("새로 추가된 답변 내용");

        assertTrue(savedAnswer.isPresent(), "데이터베이스에 저장된 질문이 존재해야 합니다.");
        assertEquals("내용이 일치해야합니다", "새로 추가된 답변 내용", savedAnswer.get().getContent());


    }


    @Test
    @DisplayName("관리자 답변 수정1")
    void putQuestionTest1() throws Exception {

        Question question=mockMember.getQuestions().getFirst();
        Answer answer =question.getAnswers().getFirst();

        ObjectMapper objectMapper = new ObjectMapper();

        ReqAnswerDto reqAnswerDto=new ReqAnswerDto("수정된 답변 내용");
        String requestBody =objectMapper.writeValueAsString(reqAnswerDto);

        ResultActions resultActions = mockMvc.perform(
                        put("/admin/dashboard/questions/{questionId}/answers/{answerId}", question.id,answer.id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(requestBody)

                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(AnswerController.class))
                .andExpect(handler().methodName("modifyAnswer"))
                .andExpect(status().isOk());

        Optional<Answer> savedAnswer = answerRepository.findByContent("수정된 답변 내용");

        assertTrue(savedAnswer.isPresent(), "데이터베이스에 저장된 질문이 존재해야 합니다.");
        assertEquals("내용이 일치해야합니다", "수정된 답변 내용", savedAnswer.get().getContent());

    }

    @Test
    @DisplayName("사용자 질문 삭제1")
    void deleteAnswerTest1() throws Exception {

        Question question=mockMember.getQuestions().getFirst();
        Answer answer =question.getAnswers().getFirst();

        ResultActions resultActions = mockMvc.perform(
                        delete("/admin/dashboard/questions/{questionId}/answers/{answerId}", question.id,answer.id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)

                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(AnswerController.class))
                .andExpect(handler().methodName("deleteAnswer"))
                .andExpect(status().isNoContent());

        Optional<Answer> savedAnswer = answerRepository.findById(answer.id);

        assertTrue(savedAnswer.isEmpty(), "데이터가 삭제되지 않았습니다..");

    }
}
