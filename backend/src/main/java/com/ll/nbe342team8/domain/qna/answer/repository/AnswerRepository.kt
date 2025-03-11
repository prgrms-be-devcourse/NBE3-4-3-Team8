package com.ll.nbe342team8.domain.qna.answer.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.question.entity.Question;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
	boolean existsByQuestionAndMemberAndContentAndCreateDateAfter(Question question, Member member, String content, LocalDateTime cutoffTime);

	List<Answer> findByQuestionOrderByCreateDateDesc(Question question);

	List<Answer> findByQuestionId(Long questionId);
}
