package com.ll.nbe342team8.member.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.category.service.CategoryService;
import com.ll.nbe342team8.domain.book.review.entity.Review;
import com.ll.nbe342team8.domain.book.review.service.ReviewService;
import com.ll.nbe342team8.domain.member.deliveryInformation.dto.DeliveryInformationDto;
import com.ll.nbe342team8.domain.member.deliveryInformation.repository.DeliveryInformationRepository;
import com.ll.nbe342team8.domain.member.deliveryInformation.service.DeliveryInformationService;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.member.controller.MemberController;
import com.ll.nbe342team8.domain.member.member.dto.PutReqMemberMyPageDto;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import com.ll.nbe342team8.global.util.JwtTestUtil;
import jakarta.servlet.http.Cookie;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Member mockMember;

    @Autowired
    MemberService memberService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    BookService bookService;

    @Autowired MemberRepository memberRepository;

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
    @DisplayName("사용자 페이지 불러오기1")
    void getMyPageTest() throws Exception {

        // ✅ 3. API 요청
        ResultActions resultActions = mockMvc.perform(
                        get("/api/auth/me/my")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // ✅ 4. 응답 검증
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("getMyPage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("010-1111-2222"))
                .andExpect(jsonPath("$.name").value("테스트 유저"))
                // 첫 번째 배송지
                .andExpect(jsonPath("$.deliveryInformationDtos[0].addressName").value("회사"))
                .andExpect(jsonPath("$.deliveryInformationDtos[0].postCode").value("67890"))
                .andExpect(jsonPath("$.deliveryInformationDtos[0].detailAddress").value("서울 서초구"))
                .andExpect(jsonPath("$.deliveryInformationDtos[0].recipient").value("홍길동"))
                .andExpect(jsonPath("$.deliveryInformationDtos[0].phone").value("010-9876-5432"))
                .andExpect(jsonPath("$.deliveryInformationDtos[0].isDefaultAddress").value(true))

                // 🚀 두 번째 배송지 검증
                .andExpect(jsonPath("$.deliveryInformationDtos[1].addressName").value("집"))
                .andExpect(jsonPath("$.deliveryInformationDtos[1].postCode").value("12345"))
                .andExpect(jsonPath("$.deliveryInformationDtos[1].detailAddress").value("서울 강남구"))
                .andExpect(jsonPath("$.deliveryInformationDtos[1].recipient").value("홍길동"))
                .andExpect(jsonPath("$.deliveryInformationDtos[1].phone").value("010-1234-5678"))
                .andExpect(jsonPath("$.deliveryInformationDtos[1].isDefaultAddress").value(false));
    }

    @Test
    @DisplayName("사용자 페이지 수정하기1")
    void putMyPageTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        PutReqMemberMyPageDto putReqMemberMyPageDto=new PutReqMemberMyPageDto("김철수","010-2222-1111");

        String requestBody =objectMapper.writeValueAsString(putReqMemberMyPageDto);


        // ✅ 3. API 요청
        ResultActions resultActions = mockMvc.perform(
                        put("/api/auth/me/my")

                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(requestBody)
                )
                .andDo(print());

        // ✅ 4. 응답 검증
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("putMyPage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("010-2222-1111"))
                .andExpect(jsonPath("$.name").value("김철수"));

    }

    @Test
    @DisplayName("사용자 작성 리뷰 불러오기1")
    void getMyReviewsTest1() throws Exception {

        Category category1 = Category.builder()
                .categoryId(1)
                .categoryName("경제/경영")
                .mall("국내도서")
                .depth1("경제/경영")
                .depth2("재테크/금융")
                .depth3("재테크")
                .depth4("부자되는법")
                .depth5(null)
                .category("국내도서 > 경제/경영 > 재테크/금융 > 재테크 > 부자되는법")
                .books(new ArrayList<>())
                .build();

        Book book1 = Book.builder()
                .title("부자 되는 법")
                .author("홍길동")
                .isbn("978-89-1234-567-8")
                .isbn13("9788912345678")
                .pubDate(LocalDate.of(2023, 10, 15))
                .priceStandard(25000)
                .pricesSales(22000)
                .stock(100)
                .status(1)
                .rating(4.5)
                .toc("1장: 시작하기\n2장: 재테크 기본기\n3장: 투자 전략")
                .coverImage("https://example.com/cover.jpg")
                .description("이 책은 부자가 되는 법을 알려주는 최고의 가이드입니다.")
                .descriptionImage("https://example.com/description.jpg")
                .salesPoint(5000L)
                .reviewCount(120L)
                .publisher("성공출판사")
                .categoryId(category1)
                .review(new ArrayList<>())
                .build();

        categoryService.create(category1);
        bookService.create(book1);

        Review review1=Review.create(book1,mockMember,"리뷰 내용1",1.0);
        Review review2=Review.create(book1,mockMember,"리뷰 내용2",2.0);
        reviewService.create(review1,1.0);
        reviewService.create(review2,2.0);
        ResultActions resultActions = mockMvc.perform(
                        get("/api/auth/me/my/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print());

        // ✅ 4. 응답 검증
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("getMemberReviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].rating").value(2.0))
                .andExpect(jsonPath("$.items[0].bookContent").value("리뷰 내용2"))
                .andExpect(jsonPath("$.items[1].rating").value(1.0))
                .andExpect(jsonPath("$.items[1].bookContent").value("리뷰 내용1"));;
    }
}
