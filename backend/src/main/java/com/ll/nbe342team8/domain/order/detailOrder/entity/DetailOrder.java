package com.ll.nbe342team8.domain.order.detailOrder.entity;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.order.order.entity.Order;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailOrder extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int bookQuantity;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}