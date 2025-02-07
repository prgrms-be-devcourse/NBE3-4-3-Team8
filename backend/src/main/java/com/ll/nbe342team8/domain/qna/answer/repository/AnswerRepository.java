package com.ll.nbe342team8.domain.qna.answer.repository;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    boolean existsByQuestionAndMemberAndContentAndCreateDateAfter(Question question, Member member, String content, LocalDateTime cutoffTime);
    List<Answer> findByQuestionOrderByCreateDateDesc(Question question);
}
