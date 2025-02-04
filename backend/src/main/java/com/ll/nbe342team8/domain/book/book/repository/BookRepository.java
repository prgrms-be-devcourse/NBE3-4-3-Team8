package com.ll.nbe342team8.domain.book.book.repository;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAll();

    Page<Book> findAll(Pageable pageable);

    Page<Book> findBooksByTitleContaining(String title, Pageable pageable);

    // 상품 조회
    Optional<Book> findByIsbn13(String isbn13);

    // 상품시 중목 확인
    boolean existsByIsbn13(String isbn13);
}
