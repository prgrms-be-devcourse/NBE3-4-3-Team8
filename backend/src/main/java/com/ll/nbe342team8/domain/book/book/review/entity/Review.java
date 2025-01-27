package com.ll.nbe342team8.domain.book.book.review.entity;

import com.ll.nbe342team8.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTime {


    String content;

    int rating;


}
