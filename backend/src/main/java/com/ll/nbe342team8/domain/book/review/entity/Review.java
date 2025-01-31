package com.ll.nbe342team8.domain.book.review.entity;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    Member member;

    String content;

    float rating;

    public void update(String content, float rating){
        this.content = content;
        this.rating = rating;
    }
}
