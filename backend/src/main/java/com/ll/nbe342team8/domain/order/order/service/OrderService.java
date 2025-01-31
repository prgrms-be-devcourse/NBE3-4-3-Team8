package com.ll.nbe342team8.domain.order.order.service;

import com.ll.nbe342team8.domain.order.order.dto.OrderDTO;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.domain.order.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderDTO> getOrdersByMemberId(Long memberId) {
        List<Order> orders = orderRepository.findByMemberId(memberId);
        return orders.stream()
                .map(order -> new OrderDTO(order.getMember().getId(),
                        order.getOrderStatus().name(),
                        order.getTotalPrice()))
                .collect(Collectors.toList());
    }
}
