package com.ll.nbe342team8.domain.qna.answer.service;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.answer.dto.ReqAnswerDto;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.answer.repository.AnswerRepository;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.standard.util.Ut;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;


    @Transactional
    public void createAnswer(Question question, Member member, ReqAnswerDto dto) {

        Answer answer = Answer.builder()
                .content(dto.content())
                .member(member)
                .question(question)
                .build();

        question.addAnswer(answer);
        answerRepository.save(answer);
    }

    @Transactional
    public void modifyAnswer(Answer answer ,ReqAnswerDto dto) {
        //이스케이프 처리한 데이터를 question 개체에 갱신
        answer.updateAnswerInfo(dto);
    }

    @Transactional
    public void deleteAnswer(Answer answer) {
        Question question = answer.getQuestion();
        question.removeAnswer(answer);
        answerRepository.delete(answer);
    }

    public Optional<Answer> findById(Long id) {
        return answerRepository.findById(id);
    }


    public List<Answer> findByQuestion(Question question) {
        return answerRepository.findByQuestionOrderByCreateDateDesc(question);
    }

    //네트워크 지연, 스팸 봇, 답변 등록 버튼 연타로 생성되는 중복 답변 방지
    public boolean existsDuplicateAnswerInShortTime(Question question, Member member, String content, Duration duration) {

        LocalDateTime cutoffTime = LocalDateTime.now().minus(duration);
        return answerRepository.existsByQuestionAndMemberAndContentAndCreateDateAfter(question, member, content, cutoffTime);
    }
}