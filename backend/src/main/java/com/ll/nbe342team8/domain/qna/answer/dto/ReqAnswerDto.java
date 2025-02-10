package com.ll.nbe342team8.domain.qna.answer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record ReqAnswerDto(@NotNull String content) {}