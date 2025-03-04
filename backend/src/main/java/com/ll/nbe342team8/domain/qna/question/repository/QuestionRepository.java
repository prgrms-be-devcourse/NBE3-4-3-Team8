package com.ll.nbe342team8.domain.qna.question.repository;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDto;
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDtoProjection;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    Page<Question> findByAnswersIsNotEmptyAndTitleContainingOrContentContaining(
            String title, String content, Pageable pageable);

    Page<Question> findByAnswersIsEmptyAndTitleContainingOrContentContaining(
            String title, String content, Pageable pageable);

    Page<Question> findByAnswersIsNotEmpty(Pageable pageable);
    Page<Question> findByAnswersIsEmpty(Pageable pageable);

    //Page<Question> findByMember(Pageable pageable, Member member);
    Page<QuestionListDtoProjection> findByMember(Pageable pageable, Member member);

    boolean existsByMemberAndTitleAndContentAndCreateDateAfter( Member member, String title,String content, LocalDateTime cutoffTime);
}