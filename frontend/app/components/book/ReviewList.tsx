// components/book/ReviewList.tsx
'use client';
import React, { useEffect, useState } from 'react';
import { Review, ReviewResponse, SortType, sortTypeLabels } from '@/types/review';
import { ReviewForm } from './ReviewForm';
import { Pagination } from '@/app/components/common/Pagination';

interface ReviewListProps {
  bookId: number;
}

export const ReviewList = ({ bookId }: ReviewListProps) => {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const pageSize = 5;
  const [sortType, setSortType] = useState<SortType>(SortType.CREATE_AT_DESC);

  const handleSortChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSortType(event.target.value as SortType);
    setCurrentPage(0);
  };

  const fetchReviews = async () => {
    if (!bookId) {
      setError('도서 ID가 없습니다.');
      return;
    }

    setIsLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/reviews/${bookId}?page=${currentPage}&pageSize=${pageSize}&sortType=${sortType}`,
      );

      if (!response.ok) {
        throw new Error('리뷰를 불러오는데 실패했습니다.');
      }

      const data: ReviewResponse = await response.json();
      setReviews(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
      setError(null);
    } catch (error) {
      setError(error instanceof Error ? error.message : '리뷰를 불러오는데 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleReviewSubmitted = () => {
    setCurrentPage(0);
    fetchReviews();
  };

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  const renderStars = (rating: number) => {
    const fullStars = Math.min(Math.floor(rating), 5);
    const emptyStars = 5 - fullStars;

    return (
      <div className="flex">
        <span className="text-yellow-500 tracking-[1px]">{'★'.repeat(fullStars)}</span>
        <span className="text-gray-300 tracking-[1px]">{'★'.repeat(emptyStars)}</span>
      </div>
    );
  };

  const formatDate = (dateString: string) => {
    try {
      if (!dateString) return '날짜 정보 없음';

      // 밀리초 부분 제거
      const cleanDateString = dateString.split('.')[0];
      const [date, time] = cleanDateString.split('T');

      if (!date || !time) return dateString;

      const [year, month, day] = date.split('-');
      const [hour, minute] = time.split(':');

      return `${year}년 ${Number(month)}월 ${Number(day)}일 ${hour}:${minute}`;
    } catch (error) {
      console.error('날짜 변환 중 오류:', error);
      return '날짜 정보 없음';
    }
  };

  useEffect(() => {
    fetchReviews();
  }, [currentPage, bookId, sortType]);

  if (isLoading) return <div className="p-6 text-center">리뷰를 불러오는 중...</div>;
  if (error) return <div className="p-6 text-center text-red-500">{error}</div>;

  return (
    <div className="p-6">
      <ReviewForm bookId={bookId} onReviewSubmitted={handleReviewSubmitted} />

      <div className="flex justify-between items-center mb-4">
        <h3 className="text-lg font-semibold">전체 리뷰 ({totalElements})</h3>

        <select
          value={sortType}
          onChange={handleSortChange}
          className="px-3 py-1 border rounded bg-white text-sm cursor-pointer hover:border-gray-400 focus:outline-none focus:border-blue-500"
        >
          {Object.entries(sortTypeLabels).map(([value, label]) => (
            <option key={value} value={value}>
              {label}
            </option>
          ))}
        </select>
      </div>

      {reviews && reviews.length > 0 ? (
        <>
          <ul className="space-y-4">
            {reviews.map((review) => (
              <li key={review.reviewId} className="border-b pb-4">
                <div className="flex justify-between items-center mb-2">
                  <span className="font-bold">{review.author}</span>
                  {renderStars(review.rating)}
                </div>
                <p className="text-gray-700 whitespace-pre-line">{review.content}</p>
                <p className="text-sm text-gray-500 mt-2">{formatDate(review.createDate)}</p>
              </li>
            ))}
          </ul>
          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            onPageChange={handlePageChange}
          />
        </>
      ) : (
        <div className="text-center py-12">
          <p className="text-gray-500">작성된 리뷰가 없습니다.</p>
        </div>
      )}
    </div>
  );
};
