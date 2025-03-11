export interface PageDto<QuestionDto> {
    currentPageNumber: number; // 현재 페이지 번호
    pageSize: number; // 한 페이지당 항목 개수
    totalPages: number; // 전체 페이지 수
    totalItems: number; // 전체 항목 수
    items: QuestionDto[]; // 질문 목록 (제네릭)
  }
  
  export interface QuestionListDto {
    id: number; // Long → number
    createDate: string; // LocalDateTime → ISO 문자열 형식 사용
    title: string; // @NotBlank → 빈 문자열 방지 필요
    content: string; // @NotBlank → 빈 문자열 방지 필요
    isAnswer: boolean;
  }

  export interface CursorPageDto<QuestionDto> {
    items: QuestionDto[]; // 데이터 목록
    nextCursor: string | null; // 다음 페이지를 위한 커서 값
    prevCursor: string | null; // 이전 페이지를 위한 커서 값
  }
  
    