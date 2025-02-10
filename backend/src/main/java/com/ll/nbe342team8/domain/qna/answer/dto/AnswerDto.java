package com.ll.nbe342team8.domain.qna.answer.dto;

import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.standard.PageDto.PageDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
@Data
@NoArgsConstructor
public class AnswerDto {
    private Long id;            // 답변의 고유 식별자

    @NotNull(message = "답변 내용은 필수입니다")
    private String content;     // 답변 내용

    private Long questionId;    // 이 답변이 달린 질문의 ID
    private LocalDateTime createDate;  // 답변 생성 시간
    private LocalDateTime modifyDate;  // 답변 수정 시간

    // Answer 엔티티를 DTO로 변환하는 생성자
    public AnswerDto(Answer answer) {
        this.id = answer.getId();
        this.content = answer.getContent();
        this.questionId = answer.getQuestion().getId();
        this.createDate = answer.getCreateDate();
        this.modifyDate = answer.getModifyDate();
    }
}