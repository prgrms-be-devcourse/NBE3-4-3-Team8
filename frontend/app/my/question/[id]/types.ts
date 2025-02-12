export interface QuestionDto {
    id: number; // Long → number
    createDate: string; // LocalDateTime → ISO 문자열 형식 사용
    modifyDate: string;
    title: string; // @NotBlank → 빈 문자열 방지 필요
    content: string; // @NotBlank → 빈 문자열 방지 필요
    isAnswer: boolean;
    answers: AnswerDto[];
  }

  export interface AnswerDto {
    id: number; // Long → number
    createDate: string; // LocalDateTime → string (ISO 형식)
    modifyDate: string; // LocalDateTime → string
    content: string; // @NotNull String → string
  }
  