package com.ll.nbe342team8.domain.order.detailOrder.service;

import com.ll.nbe342team8.domain.jwt.AuthService;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.order.detailOrder.dto.DetailOrderDto;
import com.ll.nbe342team8.domain.order.detailOrder.entity.DetailOrder;
import com.ll.nbe342team8.domain.order.detailOrder.repository.DetailOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetailOrderService {
    private final DetailOrderRepository detailOrderRepository;
    private final AuthService authService;

    public DetailOrderService(DetailOrderRepository detailOrderRepository, AuthService authService) {
        this.detailOrderRepository = detailOrderRepository;
        this.authService = authService;
    }

    // 주문상세조회
    public List<DetailOrderDto> getDetailOrdersByOrderIdAndMember(Long orderId, Member member) {
        // 레포지토리에서 orderId와 member로 주문 상세 조회
        List<DetailOrder> detailOrders = detailOrderRepository.findByOrderId(orderId);
        System.out.println("detailOrders = " + detailOrders.toString());
        // 주문 상세 정보를 DetailOrderDto로 변환하여 반환
        return detailOrders.stream()
                .map(detailOrder -> new DetailOrderDto(
                        detailOrder.getOrder().getId(),
                        detailOrder.getBook().getId(),
                        detailOrder.getBookQuantity(),
                        detailOrder.getDeliveryStatus()))
                .collect(Collectors.toList());
    }
}