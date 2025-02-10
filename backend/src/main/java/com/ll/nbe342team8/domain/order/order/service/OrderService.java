package com.ll.nbe342team8.domain.order.order.service;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository;
import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.domain.order.order.entity.Order.OrderStatus;
import com.ll.nbe342team8.domain.order.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final DetailOrderRepository detailOrderRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, DetailOrderRepository detailOrderRepository, MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.detailOrderRepository = detailOrderRepository;
        this.memberRepository = memberRepository;
    }

    public List<OrderDTO> getOrdersByMemberId(Long memberId) {
        // 회원이 존재하는지 먼저 체크
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 주문 조회
        List<Order> orders = orderRepository.findByMemberId(memberId);
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }

        // DTO로 변환하여 반환
        return orders.stream()
                .map(order -> new OrderDTO(order.getMember().getId(),
                        order.getOrderStatus().name(),
                        order.getTotalPrice()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        if (order.getOrderStatus() != OrderStatus.COMPLETE) {
            throw new IllegalStateException("주문이 완료되지 않아 삭제할 수 없습니다.");
        }

        detailOrderRepository.deleteByOrderId(orderId);
        orderRepository.delete(order);
    }

}
