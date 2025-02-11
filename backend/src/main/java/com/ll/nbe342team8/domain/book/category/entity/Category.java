package com.ll.nbe342team8.domain.book.category.entity;

import java.util.ArrayList;
import java.util.List;

import com.ll.nbe342team8.domain.book.book.entity.Book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	private Long id;

	@NotNull
	@Column(name = "category_id")
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

	@OneToMany(mappedBy = "categoryId")
	private List<Book> books = new ArrayList<>();

	private String category; // 카테고리 종류 ex) 국내도서 > 경제/경영 > 재테크/금융 > 재테크 > 부자되는법
}
