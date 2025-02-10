package com.ll.nbe342team8.domain.book.review.entity;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    Book book;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    Member member;

    @NonNull
    String content;

    @NonNull
    Double rating;

    public Review(Book book, Member member, String content, Double rating) {
        this.book = book;
        this.member = member;
        this.content = content;
        this.rating = rating;
    }

    public void update(String content, Double rating){
        this.content = content;
        this.rating = rating;
    }

    // 정적 팩토리 메서드 - 컨트롤러나 서비스 단에서 빌더 패턴을 사용하지 않고 객체를 생성할 수 있도록 함.
    public static Review create(Book book, Member member, String content, Double rating){
        return new Review(book, member, content, rating);
    }
}
