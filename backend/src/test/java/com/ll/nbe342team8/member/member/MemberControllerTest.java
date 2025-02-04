package com.ll.nbe342team8.member.member;

import com.ll.nbe342team8.domain.member.deliveryInformation.service.DeliveryInformationService;
import com.ll.nbe342team8.domain.member.member.controller.MemberController;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;

    @Autowired
    private DeliveryInformationService deliveryInformationService;

    @Test
    @DisplayName("사용자 페이지 불러오기")
    void getMyPageTest1() throws Exception{

        String email="rdh0427@naver.com";

        ResultActions resultActions = mockMvc
                .perform(
                        get("/my")

                                .contentType(
                                        new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("getMyPage"))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.resultCode").value("201-1"))
                //.andExpect(jsonPath("$.msg").value("원두가 추가되었습니다."));
    }
}
