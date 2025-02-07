package com.ll.nbe342team8.domain.qna.question.repository;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findByMember(Pageable pageable, Member member);
    boolean existsByMemberAndTitleAndContentAndCreateDateAfter( Member member, String title,String content, LocalDateTime cutoffTime);
}
