package com.ll.nbe342team8.Book.Cart;

import com.ll.nbe342team8.domain.cart.dto.CartItemRequestDto;
import com.ll.nbe342team8.domain.cart.dto.CartRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CartControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    // 데이터가 있을 경우에 테스트 시도
    @Test
    @DisplayName("장바구니 조회 테스트")
    public void testGetCart() {
        // URL: GET /cart/1
        // URL: GET /cart/memberId-id
        ResponseEntity<String> response = restTemplate.getForEntity("/cart/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("장바구니 추가 테스트")
    public void testAddCart() {
        // URL: POST /cart/1/1?quantity=2
        // URL: POST /cart/book-id/memberId-id?quantity=?
        ResponseEntity<Void> response = restTemplate.postForEntity("/cart/1/1?quantity=2", null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("장바구니 수정 테스트")
    public void testUpdateCartItem() {
        // URL: PUT /cart/1/1?quantity=3
        // URL: PUT /cart/book-id/memberId-id?quantity=?
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("/cart/1/1?quantity=3", HttpMethod.PUT, entity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /*
    @Test
    @DisplayName("장바구니 삭제 테스트")
    public void testDeleteCartItem() throws Exception {
        // URL: DELETE /cart/1 JSON body
        // URL: DELETE /cart/memberId-id JSON body
        CartItemRequestDto itemRequest = new CartItemRequestDto(1L, 1);
        CartRequestDto requestDto = new CartRequestDto(Arrays.asList(itemRequest));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CartRequestDto> entity = new HttpEntity<>(requestDto, headers);
        ResponseEntity<Void> response = restTemplate.exchange("/cart/1", HttpMethod.DELETE, entity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    */

}
