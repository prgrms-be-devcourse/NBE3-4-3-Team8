package com.ll.nbe342team8.admin;

import com.ll.nbe342team8.domain.admin.controller.AdminQuestionController;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.answer.repository.AnswerRepository;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository;
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

import java.nio.charset.StandardCharsets;
import static org.hamcrest.Matchers.hasSize;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class AdminQuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Member admin;
    private Member regular;

    @Autowired
    MemberService memberService;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private Long questionId;

    @BeforeEach
    void setup() {
        admin = new Member();
        admin.setEmail("admin@test.com");
        admin.setPassword("1234");
        admin.setName("Admin");
        admin.setOAuthId("admin123");
        admin.setMemberType(Member.MemberType.ADMIN);

        regular = new Member();
        regular.setOAuthId("user123");
        regular.setEmail("user@example.com");
        regular.setPhoneNumber("010-1234-5678");
        regular.setName("User");
        regular.setMemberType(Member.MemberType.USER);

        memberService.saveMember(admin);
        memberService.saveMember(regular);

        Question question1 = Question.builder()
                .title("Question 1")
                .content("Content 1")
                .member(regular)
                .build();

        Question question2 = Question.builder()
                .title("Question 2")
                .content("Content 2")
                .member(regular)
                .build();

        question1 = questionRepository.save(question1);
        questionRepository.save(question2);

        this.questionId = question1.getId();
        Answer answer = Answer.builder()
                .content("Answer 1")
                .question(question1)
                .member(admin)
                .build();

        answerRepository.save(answer);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new SecurityUser(admin),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("관리자 질문 불러오기")
    void getAllQuestionsTest() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                        get("/admin/dashboard/questions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdminQuestionController.class))
                .andExpect(handler().methodName("getAdminQuestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].title").value("Question 2"))
                .andExpect(jsonPath("$.items[0].hasAnswer").value(false));
        
    }

    @Test
    @DisplayName("관리자- 답변 있는 질문 불러오기")
    void getQuestionswithAnswerTest() throws Exception{

        ResultActions resultActions = mockMvc.perform(
                get("/admin/dashboard/questions")
                        .param("hasAnswer", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
        ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdminQuestionController.class))
                .andExpect(handler().methodName("getAdminQuestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].title").value("Question 1"))
                .andExpect(jsonPath("$.items[0].hasAnswer").value(true));
    }

    @Test
    @DisplayName("관리자 - 특정 질문 상세 조회")
    void getQuestionDetailTest() throws Exception{

        ResultActions resultActions = mockMvc.perform(
                        get("/admin/dashboard/questions/{id}", questionId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AdminQuestionController.class))
                .andExpect(handler().methodName("getAdminQuestion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(questionId))
                .andExpect(jsonPath("$.title").value("Question 1"))
                .andExpect(jsonPath("$.content").value("Content 1"))
                .andExpect(jsonPath("$.memberEmail").value("user@example.com"))
                .andExpect(jsonPath("$.hasAnswer").value(true))
                .andExpect(jsonPath("$.answer").exists())
                .andExpect(jsonPath("$.answer.content").value("Answer 1"));
    }
}


