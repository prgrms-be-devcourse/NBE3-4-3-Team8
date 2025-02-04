package com.ll.nbe342team8.domain.book.book.repository;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAll();

    Page<Book> findAll(Pageable pageable);

    Page<Book> findBooksByTitleContaining(String title, Pageable pageable);
}
