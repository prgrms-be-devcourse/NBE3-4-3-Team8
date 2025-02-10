package com.ll.nbe342team8.domain.qna.question.service;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository;
import com.ll.nbe342team8.standard.util.Ut;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public void createQuestion(Member member, ReqQuestionDto dto) {
        //이스케이프 처리
        String sanitizedTitle = Ut.XSSSanitizer.sanitize(dto.getTitle());
        String sanitizedContent = Ut.XSSSanitizer.sanitize(dto.getContent());
        Question question = Question.builder()
                .title(sanitizedTitle)
                .content(sanitizedContent)
                .member(member)
                .build();

        questionRepository.save(question);
    }
}
