package com.ll.nbe342team8.domain.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "cart",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_member_book",
                columnNames = {"member_id", "book_id"}
        )
)
public class Cart extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    private int quantity;

    public void updateCart(int quantity) {
        this.quantity = quantity;
    }

    public Cart(Member member, Book book, int quantity) {
        this.member = member;
        this.book = book;
        this.quantity = quantity;
    }

    public static Cart create(Book book, int quantity) {
        return new Cart(null, book, quantity);
    }

    @Override
    public String toString() {
        return "Cart{" +
               "id=" + id +
               ", member=" + member +
               ", book=" + book +
               ", quantity=" + quantity +
               '}';
    }
}
