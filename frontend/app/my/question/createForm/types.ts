export interface ReqQuestionDto {
    title: string;  // @NotBlank → 빈 문자열이면 안 됨
    content: string; // @NotNull → 반드시 값이 있어야 함
  }


export interface PostResQuestionDto {
    questions: QuestionDto[]; // List<Question> → 배열 타입으로 변환
}

export interface QuestionDto {
  id: number; // Long → number
  createDate: string; // LocalDateTime → ISO 문자열 형식 사용
  modifyDate: string;
  title: string; // @NotBlank → 빈 문자열 방지 필요
  content: string; // @NotBlank → 빈 문자열 방지 필요
  isAnswer: boolean;
}