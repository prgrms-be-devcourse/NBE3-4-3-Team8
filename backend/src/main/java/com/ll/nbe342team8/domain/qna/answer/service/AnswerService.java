package com.ll.nbe342team8.domain.qna.answer.service;

import com.ll.nbe342team8.domain.qna.answer.dto.AnswerDto;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.answer.repository.AnswerRepository;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import com.ll.nbe342team8.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    // 답변 생성
    @Transactional
    public Answer create(Long questionId, AnswerDto dto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ServiceException(404, "질문을 찾을 수 없습니다."));

        Answer answer = Answer.builder()
                .content(dto.getContent())
                .build();

        question.addAnswer(answer);
        return answerRepository.save(answer);
    }

    public Page<Answer> getAllAnswers(Pageable pageable) {
        return answerRepository.findAll(pageable);
    }

    // 답변 조회
    public Answer getAnswer(Long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> new ServiceException(404, "답변을 찾을 수 없습니다."));
    }

    // 답변 수정
    @Transactional
    public Answer update(Long answerId, AnswerDto dto) {
        // 기존 답변 조회
        Answer answer = getAnswer(answerId);

        // XSS 공격 방지를 위한 내용 sanitize
        String sanitizedContent = Ut.XSSSanitizer.sanitize(dto.getContent());

        // 답변 내용 업데이트
        answer.updateContent(sanitizedContent);

        return answerRepository.save(answer);
    }

    // 답변 삭제
    @Transactional
    public void delete(Long answerId) {
        Answer answer = getAnswer(answerId);
        answerRepository.delete(answer);
    }

}
