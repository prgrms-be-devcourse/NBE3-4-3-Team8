export interface  ReviewsResponseDto  {
    bookId: number;
    bookTitle: string;
    bookContent: string;
    reviewId: number;
    rating: number;
    createDate: string;
    modifyDate: string;
  };

  export interface PageDto<ReviewsResponseDto> {
    currentPageNumber: number; // 현재 페이지 번호
    pageSize: number; // 한 페이지당 항목 개수
    totalPages: number; // 전체 페이지 수
    totalItems: number; // 전체 항목 수
    items: ReviewsResponseDto[]; // 질문 목록 (제네릭)
  }
  