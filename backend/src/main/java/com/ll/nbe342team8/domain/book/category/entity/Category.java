package com.ll.nbe342team8.domain.book.category.entity;

import com.ll.nbe342team8.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    @NotNull
    private Integer categoryId;

    @NotNull
    private String categoryName;

    @NotNull
    private String mall;

    @NotNull
    private String Depth1;

    private String Depth2;

    private String Depth3;

    private String Depth4;

    private String Depth5;

    private String category; // 카테고리 종류 ex) 국내도서 > 경제/경영 > 재테크/금융 > 재테크 > 부자되는법
}
