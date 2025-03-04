package com.ll.nbe342team8.domain.book.category.service;

import com.ll.nbe342team8.domain.book.category.entity.Category;
import com.ll.nbe342team8.domain.book.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void create(Category category) {
        categoryRepository.save(category);
    }
}
