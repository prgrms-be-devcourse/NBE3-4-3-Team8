package com.ll.nbe342team8.domain.qna.question.repository;

import com.ll.nbe342team8.domain.qna.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
