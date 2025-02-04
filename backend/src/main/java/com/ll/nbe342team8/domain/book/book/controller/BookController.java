package com.ll.nbe342team8.domain.book.book.controller;

import org.springframework.web.bind.annotation.RestController;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {

    private BookService bookService;

    @PostMapping("/admin/books")
    public ResponseEntity<?> addBook(@RequestParam(required = false) String isbn13) {
        if (isbn13 == null) {
            return ResponseEntity.badRequest().body("ISBN13 값을 포함해야 합니다.");
        }

        return ResponseEntity.ok("요청 성공: 확인 완료.");
    }

}
