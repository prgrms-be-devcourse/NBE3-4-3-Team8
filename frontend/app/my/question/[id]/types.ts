export interface QuestionDto {
    id: number; // Long → number
    createDate: string; // LocalDateTime → ISO 문자열 형식 사용
    modifyDate: string;
    title: string; // @NotBlank → 빈 문자열 방지 필요
    content: string; // @NotBlank → 빈 문자열 방지 필요
    isAnswer: boolean;
    answers: AnswerDto[];
    genFiles: QuestionGenFileDto[];
  }

  export interface AnswerDto {
    id: number; // Long → number
    createDate: string; // LocalDateTime → string (ISO 형식)
    modifyDate: string; // LocalDateTime → string
    content: string; // @NotNull String → string
  }

  export interface QuestionGenFileDto {
    id: number;
    createDate: string; // ISO 8601 형식의 날짜 문자열
    modifyDate: string; // ISO 8601 형식의 날짜 문자열
    postId: number;
    fileName: string;
    typeCode: string;
    fileExtTypeCode: string;
    fileExtType2Code: string;
    fileSize: number;
    fileNo: number;
    fileExt: string;
    fileDateDir: string;
    originalFileName: string;
  }
  
  