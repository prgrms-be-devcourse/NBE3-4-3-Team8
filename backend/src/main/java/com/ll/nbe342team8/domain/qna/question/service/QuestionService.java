package com.ll.nbe342team8.domain.qna.question.service;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import com.ll.nbe342team8.domain.qna.question.dto.QuestionDto;
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDto;
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDtoProjection;
import com.ll.nbe342team8.domain.qna.question.dto.ReqQuestionDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.domain.qna.question.repository.QuestionRepository;
import com.ll.nbe342team8.standard.PageDto.PageDto;
import com.ll.nbe342team8.standard.util.Ut;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public Question createQuestion(Member member, ReqQuestionDto dto) {
        //이스케이프 처리
        Question question = Question.create(dto,member);
        questionRepository.save(question);

        return question;
    }

    public PageDto<QuestionListDto> getPage(Member member, int page) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        Page<QuestionListDtoProjection> paging = this.questionRepository.findByMember(pageable, member);

        Page<QuestionListDto> pagingOrderDto = paging.map(QuestionListDto::new);
        PageDto<QuestionListDto> pageDto = new PageDto<>(pagingOrderDto);
        return pageDto;

    }



    @Transactional
    public void deleteQuestion(Question question) {

        questionRepository.delete(question);
    }

    @Transactional
    public void modifyQuestion(Question question,ReqQuestionDto dto) {

        question.updateQuestionInfo(dto);
    }

    @Transactional
    public Optional<Question> findById(Long id) {

        return questionRepository.findById(id);
    }

    //수정, 삭제하려는 게시글을 사용자가 작성한지 학인
    public boolean isQuestionOwner(Member member,Question question) {

        return question.getMember().getId().equals(member.getId());
    }

    //네트워크 지연, 스팸 봇, 답변 등록 버튼 연타로 생성되는 중복 답변 방지
    public boolean existsDuplicateQuestionInShortTime(Member member,String title, String content, Duration duration) {
        LocalDateTime cutoffTime = LocalDateTime.now().minus(duration);
        return questionRepository.existsByMemberAndTitleAndContentAndCreateDateAfter( member, title, content, cutoffTime);
    }

    public void flush() {
        questionRepository.flush(); // em.flush(); 와 동일
    }
}