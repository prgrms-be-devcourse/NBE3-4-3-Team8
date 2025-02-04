// components/book/BookInfo.tsx
'use client';
import React from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import type { Book } from '@/types/book';
import { addToCart } from '@/utils/api';

interface BookInfoProps {
  book: Book;
}

export const BookInfo: React.FC<BookInfoProps> = ({ book }) => {
  const router = useRouter();

  const handleAddToCart = async () => {
    try {
      await addToCart(book.id, 1, 1);
      router.push('/cart');
    } catch (error) {
      console.error('장바구니 추가 실패', error);
    }
  };

  const averageRating =
    book.reviewCount > 0 ? (book.rating / book.reviewCount).toFixed(1) : '평점 없음';

  return (
    <div className="max-w-[800px] mx-auto px-4 py-8">
      {' '}
      {/* 전체 컨테이너 너비 증가 */}
      <div className="flex gap-8">
        {' '}
        {/* flex-row가 기본값이므로 생략 가능 */}
        {/* 도서 이미지 */}
        <div className="w-[380px] flex-shrink-0">
          {' '}
          {/* flex-shrink-0으로 크기 고정 */}
          <Image
            src={book.coverImage || '/default-book.png'}
            alt={book.title}
            width={380}
            height={500}
            className="w-full h-auto object-contain"
            priority
          />
        </div>
        {/* 도서 정보 */}
        <div className="w-[380px]">
          {' '}
          {/* 이미지와 동일한 너비 */}
          <h1 className="text-2xl font-bold">{book.title}</h1>
          <p className="text-lg mt-2">{book.author}</p>
          <p className="text-gray-600 mt-1">
            출판: {book.publisher} ({new Date(book.pubDate).toLocaleDateString()})
          </p>
          <p className="text-blue-600 mt-1">카테고리: {book.categoryId || '미정'}</p>
          <div className="bg-gray-50 p-4 rounded-lg my-4">
            <p className="line-through text-gray-500">{book.priceStandard.toLocaleString()}원</p>
            <p className="text-xl font-bold text-red-600">{book.priceSales.toLocaleString()}원</p>
            <p className="text-green-600 mt-1">무료 배송</p>
            <p className="mt-2">
              평점: {averageRating} ({book.reviewCount}개 리뷰)
            </p>
          </div>
          <div className="flex flex-col gap-2">
            <button
              className="w-full py-3 bg-blue-500 text-white rounded-md hover:bg-blue-600"
              onClick={handleAddToCart}
            >
              장바구니 담기
            </button>
            <button
              className="w-full py-3 bg-green-500 text-white rounded-md hover:bg-green-600"
              onClick={() => router.push('/cart')}
            >
              바로구매
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
