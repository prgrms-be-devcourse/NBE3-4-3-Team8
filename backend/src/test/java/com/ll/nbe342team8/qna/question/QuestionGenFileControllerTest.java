package com.ll.nbe342team8.qna.question;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.domain.qna.question.controller.QuestionController;
import com.ll.nbe342team8.domain.qna.question.controller.QuestionGenFileController;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.entity.QuestionGenFile;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionGenFileRepository;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository;
import com.ll.nbe342team8.domain.qna.question.service.QuestionService;
import com.ll.nbe342team8.global.config.AppConfig;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class QuestionGenFileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Member mockMember;

    @Autowired
    MemberService memberService;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionGenFileRepository questionGenFileRepository;

    @Autowired
    HttpServletRequest request;
    
    // 해당 경로에 테스트용 업로드 이미지 데이터가 필요함
    String filePath="c:/temp/glog_dev/postGenFileTest/testImage.png";

    @BeforeEach
    void setup() {
        // ✅ Mock Member 생성
        mockMember = new Member();
        mockMember.setOAuthId("31313");
        mockMember.setPhoneNumber("010-1111-2222");
        mockMember.setName("테스트 유저");

        Question question = Question.builder()
                .title("제목1")
                .content("내용1")
                .member(mockMember)
                .build();

        //Mock Security Context (인증된 사용자 정보 설정)
        mockMember.setQuestions(new ArrayList<>(List.of(question)));

        memberService.saveMember(mockMember);

        questionRepository.save(question);

        question = questionRepository.findByIdWithGenFiles(question.getId()).orElseThrow();

        QuestionGenFile postGenFile = question.addGenFile("attachment", filePath);

        questionGenFileRepository.save(postGenFile);

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

    @Test
    @DisplayName("사용자 이미지 불러오기")
    void getimages() throws Exception {


        Question question = questionRepository.findByIdWithGenFiles(mockMember.getQuestions().getFirst().getId()).orElseThrow();
        QuestionGenFile questionGenFile=question.getGenFiles().getFirst();


        // ✅ 3. API 요청
        ResultActions resultActions = mockMvc.perform(
                        get("/my/question/genFile/download/{questionId}/{fileNo}",question.id,questionGenFile.fileNo)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)

                )
                .andDo(print());

        // ✅ 4. 응답 검증
        resultActions
                .andExpect(handler().handlerType(QuestionGenFileController.class))
                .andExpect(handler().methodName("loadImage"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String expectedContentType = request.getServletContext().getMimeType(filePath);
                    expectedContentType = expectedContentType != null ? expectedContentType : "application/octet-stream" ;
                    assertThat(result.getResponse().getContentType()).isEqualTo(expectedContentType);
                });
    }

    @Test
    @DisplayName("사용자 이미지 추가하기")
    void postimages() throws Exception {


        Question question = questionRepository.findByIdWithGenFiles(mockMember.getQuestions().getFirst().getId()).orElseThrow();
        File file = new File(filePath);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",  // 컨트롤러의 @RequestParam("file")과 동일한 이름
                file.getName(),
                "image/png",  // MIME 타입 설정
                new FileInputStream(file)  // 파일 읽기
        );

        String contentType = request.getServletContext().getMimeType(filePath) != null
                ? request.getServletContext().getMimeType(filePath)
                : "application/octet-stream";


        ResultActions resultActions = mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/my/question/genFile/{questionId}/attachment", question.id)
                                .file(multipartFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .with(request -> {
                                    request.setMethod("POST");
                                    return request;
                                })
                )
                .andDo(print());


        // ✅ 4. 응답 검증
        resultActions
                .andExpect(handler().handlerType(QuestionGenFileController.class))
                .andExpect(handler().methodName("makeNewFile"))
                .andExpect(status().isCreated());

        Question savedQuestion = questionRepository.findByIdWithGenFiles(mockMember.getQuestions().getFirst().getId()).orElseThrow();
        QuestionGenFile savedQuestionGenFile=question.getGenFiles().getFirst();

        Assertions.assertEquals(2, savedQuestion.getGenFiles().size(), "데이터베이스에 저장된 질문이 존재해야 합니다.");

    }

    @Test
    @DisplayName("사용자 이미지 불러오기")
    void deleteImages() throws Exception {


        Question question = questionRepository.findByIdWithGenFiles(mockMember.getQuestions().getFirst().getId()).orElseThrow();
        QuestionGenFile questionGenFile=question.getGenFiles().getFirst();


        // ✅ 3. API 요청
        ResultActions resultActions = mockMvc.perform(
                        delete("/my/question/genFile/{questionId}/{fileNo}/attachment",question.id,questionGenFile.fileNo)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // ✅ 4. 응답 검증
        resultActions
                .andExpect(handler().handlerType(QuestionGenFileController.class))
                .andExpect(handler().methodName("deleteImageFile"))
                .andExpect(status().isNoContent());

    }
}
