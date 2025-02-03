package com.ll.nbe342team8.domain.book.category.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String Depth1;

    private String Depth2;

    private String Depth3;

    private String Depth4;

    private String Depth5;

    @OneToMany(mappedBy = "categoryId", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();
}
