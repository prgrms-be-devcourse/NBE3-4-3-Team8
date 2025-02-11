package com.ll.nbe342team8.domain.qna.answer.controller;

import com.ll.nbe342team8.domain.qna.answer.dto.AnswerDto;
import com.ll.nbe342team8.domain.qna.answer.entity.Answer;
import com.ll.nbe342team8.domain.qna.answer.service.AnswerService;
import com.ll.nbe342team8.standard.PageDto.PageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/questions/{questionId}/answer")
    public ResponseEntity<AnswerDto> createAnswer(
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerDto answerDto) {
        Answer answer = answerService.create(questionId, answerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AnswerDto(answer));
    }

    // 답변 수정
    @PutMapping("/answers/{answerId}")
    public ResponseEntity<AnswerDto> updateAnswer(
            @PathVariable Long answerId,
            @Valid @RequestBody AnswerDto answerDto) {
        Answer updatedAnswer = answerService.update(answerId, answerDto);
        return ResponseEntity.ok(new AnswerDto(updatedAnswer));
    }

    // 답변 삭제
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        answerService.delete(answerId);
        return ResponseEntity.noContent().build();
    }
}
