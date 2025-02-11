package com.ll.nbe342team8.domain.qna.answer.service;

import com.ll.nbe342team8.domain.qna.answer.dto.AnswerDto;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.answer.repository.AnswerRepository;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import com.ll.nbe342team8.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public Answer create(Long questionId, AnswerDto dto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException(404, "질문을 찾을 수 없습니다."));
        // 중복 답변 방지
        if (!question.getAnswers().isEmpty()) {
            throw new ServiceException(400, "이미 답변이 존재하는 질문입니다.");
        }
        // XSS 방지
        String sanitizedContent = Ut.XSSSanitizer.sanitize(dto.getContent());
        Answer answer = Answer.builder()
                .content(sanitizedContent)
                .build();
        question.addAnswer(answer);
        return answerRepository.save(answer);
    }

    @Transactional
    public Answer update(Long answerId, AnswerDto dto) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException(404, "답변을 찾을 수 없습니다."));
        String sanitizedContent = Ut.XSSSanitizer.sanitize(dto.getContent());
        answer.updateContent(sanitizedContent);
        return answerRepository.save(answer);
    }

    @Transactional
    public void delete(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException(404, "답변을 찾을 수 없습니다."));
        answerRepository.delete(answer);
    }
}
