package com.ll.nbe342team8.domain.admin.controller;

import com.ll.nbe342team8.domain.admin.dto.AdminQuestionDto;
import com.ll.nbe342team8.domain.admin.service.AdminQuestionService;
import com.ll.nbe342team8.domain.qna.question.entity.Question;
import com.ll.nbe342team8.standard.PageDto.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/dashboard")
public class AdminQuestionController {

    private final AdminQuestionService adminQuestionService;

    @GetMapping("/questions")
    public ResponseEntity<PageDto<AdminQuestionDto>> getAdminQuestions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean hasAnswer,
            @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable)
    {
        PageDto<AdminQuestionDto> questions = adminQuestionService.getQuestionsForAdmin(keyword,hasAnswer,pageable);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<?> getAdminQuestion(@PathVariable Long id) {
        Question question = adminQuestionService.getQuestionById(id);
        AdminQuestionDto dto = new AdminQuestionDto(question);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        adminQuestionService.deleteQuestion(id);
        return ResponseEntity.ok().build();
    }
}
