package com.ll.nbe342team8.domain.order.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.domain.order.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private OrderService orderService;  // mock 객체로 생성

    @Test
    @DisplayName("주문 조회 테스트")
    @WithMockUser(username = "testUser", roles = "USER")
    void getOrdersByMember() throws Exception {
        // Given
        Member member = new Member();
        member.setId(1L);

        Order order = new Order(member, Order.OrderStatus.ORDERED, 10000L);  // Order 객체 생성
        Page<Order> orderPage = new PageImpl<>(List.of(order));  // Page 객체 생성

        when(orderService.getOrdersByMember(any(Member.class), any(Pageable.class)))
                .thenReturn(orderPage.map(orderItem -> new OrderDTO(
                        orderItem.getId(),
                        orderItem.getOrderStatus().name(),
                        orderItem.getTotalPrice(),
                        orderItem.getCreateDate()
                )));

        mockMvc.perform(get("/my/orders")
                        .principal(() -> "testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());  // JSON 응답에 content가 있는지 확인
    }
}