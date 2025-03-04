package com.ll.nbe342team8.domain.book.review.repository;

import com.ll.nbe342team8.domain.book.review.entity.Review
import com.ll.nbe342team8.domain.member.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long> {
     fun findAllByBookId(bookId: Long) : MutableList<Review>

     fun findAllByBookId(bookId: Long, pageable: Pageable): Page<Review>

     fun findByMember(pageable: Pageable?, member: Member?): Page<Review?>?
}
