export interface ReqQuestionDto {
    title: string;  // @NotBlank → 빈 문자열이면 안 됨
    content: string; // @NotNull → 반드시 값이 있어야 함
  }