package com.ll.nbe342team8.domain.admin.service;

import com.ll.nbe342team8.domain.admin.dto.AdminQuestionDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository;
import com.ll.nbe342team8.global.exceptions.ServiceException;
import com.ll.nbe342team8.standard.PageDto.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminQuestionService {
    private final QuestionRepository questionRepository;

    public PageDto<AdminQuestionDto> getQuestionsForAdmin(String keyword, Boolean hasAnswer, Pageable pageable) {
        Page<Question> questionPage = fetchQuestions(keyword, hasAnswer, pageable);
        return new PageDto<>(questionPage.map(AdminQuestionDto::new));
    }

    //검색 조건(키워드, 답변 상태)에 맞는 질문 목록을 조회
    private Page<Question> fetchQuestions(String keyword, Boolean hasAnswer, Pageable pageable) {
        if (isBlank(keyword) && hasAnswer == null) {
            return questionRepository.findAll(pageable);
        }

        if (!isBlank(keyword)) {
            return searchByKeywordAndAnswerStatus(keyword.trim(), hasAnswer, pageable);
        }

        return searchByAnswerStatus(hasAnswer, pageable);
    }

    //문자열이 비어있는지 확인
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    //키워드 및 답변 상태에 따라 질문 검색
    private Page<Question> searchByKeywordAndAnswerStatus(String keyword, Boolean hasAnswer, Pageable pageable) {
        if (hasAnswer == null) {
            return questionRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
        }
        return hasAnswer
                ? questionRepository.findByAnswersIsNotEmptyAndTitleContainingOrContentContaining(keyword, keyword, pageable)
                : questionRepository.findByAnswersIsEmptyAndTitleContainingOrContentContaining(keyword, keyword, pageable);
    }

    // 답변 상태에 따라 질문 검색
    private Page<Question> searchByAnswerStatus(Boolean hasAnswer, Pageable pageable) {
        return hasAnswer
                ? questionRepository.findByAnswersIsNotEmpty(pageable)
                : questionRepository.findByAnswersIsEmpty(pageable);
    }

    //ID를 기반으로 특정 질문을 조회
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ServiceException(404, "해당 질문을 찾을 수 없습니다."));
    }


    //관리자 권한으로 질문 삭제
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteQuestion(Long id) {
        Question question = getQuestionById(id);
        questionRepository.delete(question);
    }
}
