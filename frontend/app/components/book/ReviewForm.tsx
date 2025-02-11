'use client';
import React, { useState } from 'react';
import { createReview } from '@/utils/api.js';

interface ReviewFormProps {
  bookId: number;
  onReviewSubmitted: () => void;
}

export const ReviewForm = ({ bookId, onReviewSubmitted }: ReviewFormProps) => {
  const [rating, setRating] = useState<number>(5);
  const [content, setContent] = useState<string>('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError(null);
    try {
      await createReview(bookId, { rating, content });
      setContent('');
      setRating(5);
      onReviewSubmitted();
    } catch (error) {
      setError(error instanceof Error ? error.message : '리뷰 작성에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
      <form onSubmit={handleSubmit} className="mb-8 p-4 bg-gray-50 rounded-lg">
        <h3 className="text-lg font-semibold mb-4">리뷰 작성</h3>
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-2">평점</label>
          <select
              value={rating}
              onChange={(e) => setRating(Number(e.target.value))}
              className="w-full p-2 border rounded-md"
          >
            <option value="5">★★★★★</option>
            <option value="4">★★★★☆</option>
            <option value="3">★★★☆☆</option>
            <option value="2">★★☆☆☆</option>
            <option value="1">★☆☆☆☆</option>
          </select>
        </div>
        <div className="mb-4">
          <label className="block text-sm font-medium text-gray-700 mb-2">리뷰 내용</label>
          <textarea
              value={content}
              onChange={(e) => setContent(e.target.value)}
              className="w-full p-2 border rounded-md h-24 resize-none"
              placeholder="리뷰를 작성해주세요"
              required
          />
        </div>
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        <button
            type="submit"
            disabled={isSubmitting || !content.trim()}
            className="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 disabled:bg-gray-300 disabled:cursor-not-allowed"
        >
          {isSubmitting ? '작성 중...' : '리뷰 작성'}
        </button>
      </form>
  );
};