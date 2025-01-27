package com.ll.nbe342team8.domain.qna.answer.repository;

import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
