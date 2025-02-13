// types/review.ts
export interface Review {
  bookId: number;
  reviewId: number;
  author: string;
  content: string;
  rating: number;
  createDate: string;
}

export interface ReviewResponse {
  content: Review[];
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  size: number;
  number: number;
}

export enum SortType {
  CREATE_AT_DESC = 'CREATE_AT_DESC',
  CREATE_AT_ASC = 'CREATE_AT_ASC',
  RATING_DESC = 'RATING_DESC',
  RATING_ASC = 'RATING_ASC',
}

export const sortTypeLabels: Record<SortType, string> = {
  [SortType.CREATE_AT_DESC]: '최신순',
  [SortType.CREATE_AT_ASC]: '오래된순',
  [SortType.RATING_DESC]: '평점 높은순',
  [SortType.RATING_ASC]: '평점 낮은순',
};