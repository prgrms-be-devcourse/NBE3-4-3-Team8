package com.ll.nbe342team8.domain.qna.question.repository;

import com.ll.nbe342team8.domain.qna.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    Page<Question> findByAnswersIsNotEmptyAndTitleContainingOrContentContaining(
            String title, String content, Pageable pageable);

    Page<Question> findByAnswersIsEmptyAndTitleContainingOrContentContaining(
            String title, String content, Pageable pageable);

    Page<Question> findByAnswersIsNotEmpty(Pageable pageable);
    Page<Question> findByAnswersIsEmpty(Pageable pageable);
}
