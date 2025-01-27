package com.ll.nbe342team8.domain.book.category.repository;

import com.ll.nbe342team8.domain.book.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
