package com.ll.nbe342team8.domain.qna.question.dto;

import com.ll.nbe342team8.domain.qna.answer.dto.AnswerDto;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//질문 목록 조회시 사용
public record QuestionListDto(

        @NotNull Long id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        String title,
        String content,
        Boolean isAnswer


) {
    public QuestionListDto(Question question) {

        this(
                question.getId(),
                question.getCreateDate(),
                question.getModifyDate(),
                question.getTitle(),
                question.getContent(),
                question.getIsAnswer()
        );
    }

    public QuestionListDto(QuestionListDtoProjection questionListDtoProjection) {

        this(
                questionListDtoProjection.getId(),
                questionListDtoProjection.getCreateDate(),
                questionListDtoProjection.getModifyDate(),
                questionListDtoProjection.getTitle(),
                questionListDtoProjection.getContent(),
                questionListDtoProjection.getIsAnswer()
        );
    }
}
