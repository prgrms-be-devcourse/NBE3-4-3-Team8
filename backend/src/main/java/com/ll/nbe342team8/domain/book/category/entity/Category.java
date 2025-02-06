package com.ll.nbe342team8.domain.book.category.entity;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.global.jpa.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private String depth1;

    private String depth2;

    private String depth3;

    private String depth4;

    private String depth5;

    @OneToMany(mappedBy = "categoryId", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    private String category; // 카테고리 종류 ex) 국내도서 > 경제/경영 > 재테크/금융 > 재테크 > 부자되는법
}
