package com.ll.nbe342team8.domain.cart.repository

import com.ll.nbe342team8.domain.book.book.entity.Book
import com.ll.nbe342team8.domain.cart.entity.Cart
import com.ll.nbe342team8.domain.member.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CartRepository : JpaRepository<Cart, Long> {
    fun findAllByMember(member: Member): MutableList<Cart>

    fun findByMemberAndBook(member: Member, book: Book): Optional<Cart>

    fun deleteByMember(member: Member)
}
