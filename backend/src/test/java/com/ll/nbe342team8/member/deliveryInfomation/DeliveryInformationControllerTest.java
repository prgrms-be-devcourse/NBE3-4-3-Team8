package com.ll.nbe342team8.member.deliveryInfomation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe342team8.domain.member.deliveryInformation.controller.DeliveryInformationController;
import com.ll.nbe342team8.domain.member.deliveryInformation.dto.ReqDeliveryInformationDto;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.deliveryInformation.repository.DeliveryInformationRepository;
import com.ll.nbe342team8.domain.member.deliveryInformation.service.DeliveryInformationService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;

import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import static org.hamcrest.Matchers.not;

import com.ll.nbe342team8.domain.qna.question.controller.QuestionController;
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

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DeliveryInformationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Member mockMember;

    @Autowired
    DeliveryInformationService deliveryInformationService;

    @Autowired
    MemberService memberService;

    @Autowired
    DeliveryInformationRepository deliveryInformationRepository;

    @BeforeEach
    void setup() {
        // ✅ Mock Member 생성
        mockMember = new Member();
        mockMember.setOAuthId("31313");
        mockMember.setPhoneNumber("010-1111-2222");
        mockMember.setName("테스트 유저");

        DeliveryInformation deliveryInformation1=DeliveryInformation.builder()

                .phone("010-1234-5678")
                .detailAddress("서울 강남구")
                .isDefaultAddress(false)
                .postCode("12345")
                .recipient("홍길동")
                .addressName("집")
                .member(mockMember)
                .build();
        DeliveryInformation deliveryInformation2=DeliveryInformation.builder()

                .phone("010-9876-5432")
                .detailAddress("서울 서초구")
                .isDefaultAddress(true)
                .postCode("67890")
                .recipient("홍길동")
                .addressName("회사")
                .member(mockMember)
                .build();

        //Mock Security Context (인증된 사용자 정보 설정)
        mockMember.setDeliveryInformations(new ArrayList<>(List.of(deliveryInformation1, deliveryInformation2)));

        memberService.saveMember(mockMember);

        // ✅ Security Context에 인증 정보 추가
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
    @DisplayName("사용자 배송 정보 추가1")
    void postDeliveryInformationTest1() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        ReqDeliveryInformationDto reqDeliveryInformationDto=new ReqDeliveryInformationDto(999L,"별장","48084","부산 해운대구 우동","김철수","010-1234-5678",false);

        String requestBody =objectMapper.writeValueAsString(reqDeliveryInformationDto);

        ResultActions resultActions = mockMvc.perform(
                        post("/my/deliveryInformation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(requestBody)
                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(DeliveryInformationController.class))
                .andExpect(handler().methodName("postDeliveryInformation"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deliveryInformationDtos[*].phone").value(hasItem("010-1234-5678")))
                .andExpect(jsonPath("$.deliveryInformationDtos[*].detailAddress").value(hasItem("부산 해운대구 우동")))
                .andExpect(jsonPath("$.deliveryInformationDtos[*].isDefaultAddress").value(hasItem(false)))
                .andExpect(jsonPath("$.deliveryInformationDtos[*].postCode").value(hasItem("48084")))
                .andExpect(jsonPath("$.deliveryInformationDtos[*].recipient").value(hasItem("김철수")))
                .andExpect(jsonPath("$.deliveryInformationDtos[*].addressName").value(hasItem("별장")));

    }

    @Test
    @DisplayName("사용자 배송 정보 추가2")
    void postDeliveryInformationTest2() throws Exception {

        for(int i=3;i<=5;i++) {
            String id=Integer.toString(i);
            DeliveryInformation deliveryInformation=DeliveryInformation.builder()
                    .phone("010-1234-5678")
                    .detailAddress("서울 강남구")
                    .isDefaultAddress(false)
                    .postCode("12345")
                    .recipient("홍길동")
                    .addressName("집"+id)
                    .member(mockMember)
                    .build();
                    mockMember.getDeliveryInformations().add(deliveryInformation);
        }
        ObjectMapper objectMapper = new ObjectMapper();

        ReqDeliveryInformationDto reqDeliveryInformationDto=new ReqDeliveryInformationDto(999L,"별장","48084","부산 해운대구 우동","김철수","010-1234-5678",false);

        String requestBody =objectMapper.writeValueAsString(reqDeliveryInformationDto);

        ResultActions resultActions = mockMvc.perform(
                        post("/my/deliveryInformation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(requestBody)
                )
                .andDo(print());

        //배송지가 5개 일때 추가하면 오류 발생
        resultActions
                .andExpect(handler().handlerType(DeliveryInformationController.class))
                .andExpect(handler().methodName("postDeliveryInformation"))
                .andExpect(status().isConflict());

    }



    @Test
    @DisplayName("사용자 배송 정보 수정1")
    void putDeliveryInformationTest1() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        ReqDeliveryInformationDto reqDeliveryInformationDto=new ReqDeliveryInformationDto(1L,"부모님댁","12332","충청북도 해운대구 우동","박철완","010-3232-1588",false);

        String requestBody =objectMapper.writeValueAsString(reqDeliveryInformationDto);

        Long id=1L;
        ResultActions resultActions = mockMvc.perform(
                        put("/my/deliveryInformation/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(requestBody)

                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(DeliveryInformationController.class))
                .andExpect(handler().methodName("putDeliveryInformation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryInformationDtos[*].phone").value(hasItem("010-3232-1588")))
                .andExpect(jsonPath("$.deliveryInformationDtos[*].detailAddress").value(hasItem("충청북도 해운대구 우동")))
                .andExpect(jsonPath("$.deliveryInformationDtos[*].isDefaultAddress").value(hasItem(false)))
                .andExpect(jsonPath("$.deliveryInformationDtos[*].postCode").value(hasItem("12332")))
                .andExpect(jsonPath("$.deliveryInformationDtos[*].recipient").value(hasItem("박철완")))
                .andExpect(jsonPath("$.deliveryInformationDtos[*].addressName").value(hasItem("부모님댁")));

    }

    @Test
    @DisplayName("사용자 질문 삭제1")
    void deleteDeliveryInformationTest1() throws Exception {

        Long id=1L;

        ResultActions resultActions = mockMvc.perform(
                        delete("/my/deliveryInformation/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)

                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(DeliveryInformationController.class))
                .andExpect(handler().methodName("deleteDeliveryInformation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryInformationDtos[*].id", not(hasItem(1))));



    }
}
