'use client';
import React, { useEffect, useState } from 'react';
import { Review, ReviewResponse, SortType, sortTypeLabels } from '@/types/review';
import { ReviewForm } from './ReviewForm';
import { Pagination } from '@/app/components/common/Pagination';
import { deleteReview, updateReview, fetchCurrentUser } from '@/utils/api';

export const ReviewList = ({ bookId }: { bookId: number }) => {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const pageSize = 5;
  const [sortType, setSortType] = useState<SortType>('CREATE_AT_DESC');

  // 현재 로그인한 사용자의 ID를 상태로 저장
  const [currentUserId, setCurrentUserId] = useState<number | null>(null);

  // 현재 사용자 정보를 API 호출을 통해 가져오기
  useEffect(() => {
    const getCurrentUser = async () => {
      try {
        const userData = await fetchCurrentUser();
        // MemberDto에 id 필드가 있다고 가정
        setCurrentUserId(userData.id);
      } catch (error) {
        console.error('현재 사용자 정보를 불러오는데 실패했습니다.', error);
      }
    };
    getCurrentUser();
  }, []);

  const fetchReviews = async () => {
    if (!bookId) {
      setError('도서 ID가 없습니다.');
      return;
    }
    setIsLoading(true);
    try {
      const response = await fetch(
          `http://localhost:8080/reviews/${bookId}?page=${currentPage}&pageSize=${pageSize}&reviewSortType=${sortType}`
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

  // 리뷰 삭제: 버튼 클릭 시 로그인 멤버와 리뷰 작성자 정보를 콘솔에 출력
  const handleDelete = async (reviewId: number, reviewMemberId: number) => {
    console.log(`삭제 버튼 클릭 - currentUserId: ${currentUserId}, reviewMemberId: ${reviewMemberId}`);
    if (currentUserId === null) {
      alert("사용자 정보를 불러오지 못했습니다.");
      return;
    }
    if (currentUserId !== reviewMemberId) {
      alert("본인의 리뷰만 삭제할 수 있습니다.");
      return;
    }
    if (!window.confirm("리뷰를 삭제하시겠습니까?")) return;
    try {
      await deleteReview(reviewId);
      fetchReviews();
    } catch (err) {
      console.error("리뷰 삭제 중 오류 발생:", err);
    }
  };

  // 리뷰 수정: 버튼 클릭 시 로그인 멤버와 리뷰 작성자 정보를 콘솔에 출력
  const handleUpdate = async (
      reviewId: number,
      oldContent: string,
      oldRating: number,
      reviewMemberId: number
  ) => {
    console.log(`수정 버튼 클릭 - currentUserId: ${currentUserId}, reviewMemberId: ${reviewMemberId}`);
    if (currentUserId === null) {
      alert("사용자 정보를 불러오지 못했습니다.");
      return;
    }
    if (currentUserId !== reviewMemberId) {
      alert("본인의 리뷰만 수정할 수 있습니다.");
      return;
    }
    const newContent = prompt("수정할 내용을 입력하세요", oldContent);
    if (newContent === null) return;
    const newRatingStr = prompt("새로운 평점을 입력하세요 (1~5)", oldRating.toString());
    if (newRatingStr === null) return;
    const newRating = Number(newRatingStr);
    if (isNaN(newRating) || newRating < 1 || newRating > 5) {
      alert("평점은 1부터 5 사이의 숫자여야 합니다.");
      return;
    }
    try {
      await updateReview(reviewId, { content: newContent, rating: newRating });
      fetchReviews();
    } catch (err) {
      console.error("리뷰 수정 중 오류 발생:", err);
    }
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
              onChange={(e) => {
                setSortType(e.target.value as SortType);
                setCurrentPage(0);
              }}
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
                    <li key={review.reviewId} className="border-b pb-4 relative">
                      <div className="flex justify-between items-center mb-2">
                        <span className="font-bold">{review.author}</span>
                        {renderStars(review.rating)}
                      </div>
                      <p className="text-gray-700 whitespace-pre-line">{review.content}</p>
                      <p className="text-sm text-gray-500 mt-2">{formatDate(review.createDate)}</p>
                      {/* 모든 리뷰 항목에 수정/삭제 버튼을 항상 표시 */}
                      <div className="absolute bottom-2 right-2 flex gap-2">
                        <button
                            onClick={() =>
                                handleUpdate(review.reviewId, review.content, review.rating, review.memberId)
                            }
                            className="px-2 py-1 text-sm bg-green-500 text-white rounded hover:bg-green-600"
                        >
                          수정
                        </button>
                        <button
                            onClick={() => handleDelete(review.reviewId, review.memberId)}
                            className="px-2 py-1 text-sm bg-red-500 text-white rounded hover:bg-red-600"
                        >
                          삭제
                        </button>
                      </div>
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