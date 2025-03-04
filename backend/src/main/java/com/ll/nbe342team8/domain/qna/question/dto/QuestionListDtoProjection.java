package com.ll.nbe342team8.domain.qna.question.dto;

import java.time.LocalDateTime;

public interface QuestionListDtoProjection {
    Long getId();
    LocalDateTime getCreateDate();
    LocalDateTime getModifyDate();
    String getTitle();
    String getContent();
    Boolean getIsAnswer();
}