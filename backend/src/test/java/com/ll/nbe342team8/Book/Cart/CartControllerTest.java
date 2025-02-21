package com.ll.nbe342team8.Book.Cart;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.book.type.BookSortType;
import com.ll.nbe342team8.domain.cart.entity.Cart;
import com.ll.nbe342team8.domain.cart.repository.CartRepository;
import com.ll.nbe342team8.domain.cart.service.CartService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.domain.oauth.SecurityUser;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    // EntityManager를 주입받아 영속 상태의 엔티티를 얻습니다.
    @Autowired
    private EntityManager entityManager;

    // 테스트용 SecurityUser 빈 (TestSecurityConfig에서 제공한 빈)
    @Autowired
    private SecurityUser testSecurityUser;

    private Book testBook;

    /**
     * 각 테스트 실행 전에 해당 회원의 장바구니를 초기화하고,
     * 테스트용 도서를 미리 설정합니다.
     */
    @BeforeEach
    void setUp() {
        cartRepository.deleteByMember(testSecurityUser.getMember());
        testBook = bookService.getAllBooks(0, 1, BookSortType.PUBLISHED_DATE)
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트용 도서를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("장바구니 조회 테스트")
    void testGetCart() throws Exception {
        // 먼저 장바구니에 상품 추가
        String addCartJson = createCartRequestJson(testBook.getId(), 1, true);
        mockMvc.perform(post("/cart")
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addCartJson))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(get("/cart")
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        List<Cart> cartList = cartService.findCartByMember(testSecurityUser.getMember());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(testBook.getId()))
                .andExpect(jsonPath("$[0].quantity").value(1));
    }

    @Test
    @DisplayName("장바구니 추가 테스트")
    void testAddCart() throws Exception {
        String addCartJson = createCartRequestJson(testBook.getId(), 2, true);
        mockMvc.perform(post("/cart")
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addCartJson))
                .andDo(print())
                .andExpect(status().isOk());

        List<Cart> cartList = cartService.findCartByMember(testSecurityUser.getMember());
        assertThat(cartList).hasSize(1);
        assertThat(cartList.get(0).getBook().getId()).isEqualTo(testBook.getId());
        assertThat(cartList.get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("장바구니 수정 테스트")
    void testUpdateCart() throws Exception {
        // 우선 상품을 장바구니에 추가합니다.
        String addCartJson = createCartRequestJson(testBook.getId(), 1, true);
        mockMvc.perform(post("/cart")
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addCartJson))
                .andExpect(status().isOk());

        // update 요청 전에 회원 엔티티를 영속 상태로 재조회합니다.
        Member managedMember = entityManager.find(Member.class, testSecurityUser.getMember().getId());
        // 새 SecurityUser를 생성해 update 요청에 사용합니다.
        SecurityUser managedUser = new SecurityUser(managedMember);

        // 수정 요청: 수량을 3으로 업데이트 (isAddToCart:false)
        String updateCartJson = createCartRequestJson(testBook.getId(), 3, false);
        ResultActions resultActions = mockMvc.perform(put("/cart")
                        .with(SecurityMockMvcRequestPostProcessors.user(managedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateCartJson))
                .andDo(print());

        resultActions.andExpect(status().isOk());

        // update 후에도 영속성 컨텍스트 내에서 member를 재조회하여 검증합니다.
        Member updatedMember = entityManager.find(Member.class, managedMember.getId());
        List<Cart> cartList = cartService.findCartByMember(updatedMember);
        assertThat(cartList).hasSize(1);
        assertThat(cartList.get(0).getQuantity()).isEqualTo(3);
    }

    @Test
    @DisplayName("장바구니 삭제 테스트")
    void testDeleteCartItem() throws Exception {
        // 먼저 상품을 장바구니에 추가합니다.
        String addCartJson = createCartRequestJson(testBook.getId(), 1, true);
        mockMvc.perform(post("/cart")
                        .with(SecurityMockMvcRequestPostProcessors.user(testSecurityUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addCartJson))
                .andExpect(status().isOk());

        // 삭제 요청 전에 회원 엔티티를 영속 상태로 재조회합니다.
        Member managedMember = entityManager.find(Member.class, testSecurityUser.getMember().getId());
        SecurityUser managedUser = new SecurityUser(managedMember);

        // 삭제 요청: 수량 0, isAddToCart:false
        String deleteCartJson = createCartRequestJson(testBook.getId(), 0, false);
        ResultActions resultActions = mockMvc.perform(delete("/cart")
                        .with(SecurityMockMvcRequestPostProcessors.user(managedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteCartJson))
                .andDo(print());

        resultActions.andExpect(status().isOk());

        // 삭제 후 검증
        Member afterDeleteMember = entityManager.find(Member.class, managedMember.getId());
        List<Cart> cartList = cartService.findCartByMember(afterDeleteMember);
        assertThat(cartList).isEmpty();
    }

    @Test
    @DisplayName("익명 장바구니 조회 테스트")
    void testGetAnonymousCart() throws Exception {
        String cartRequestJson = createCartRequestJson(testBook.getId(), 2, true);
        ResultActions resultActions = mockMvc.perform(post("/cart/anonymous")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartRequestJson))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(testBook.getId()))
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

    private String createCartRequestJson(Long bookId, int quantity, boolean isAddToCart) {
        return """
                {
                    "cartItems": [
                        {
                            "bookId": %d,
                            "quantity": %d,
                            "isAddToCart": %b
                        }
                    ]
                }
                """.formatted(bookId, quantity, isAddToCart);
    }
}
