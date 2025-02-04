// components/book/BookInfo.tsx
'use client';
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import type { Book } from '@/types/book';
import { addToCart } from '@/utils/api';
import { getMemberId, isLoggedIn } from '@/utils/auth';
import { AddToCartButton } from '@/app/components/common/AddToCartButton';

interface BookInfoProps {
  book: Book;
}

export const BookInfo: React.FC<BookInfoProps> = ({ book }) => {
  const [quantity, setQuantity] = useState(1); // 수량 상태 추가
  const router = useRouter();

  // 수량 변경 핸들러
  const handleQuantityChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(e.target.value);
    if (value > 0) {
      setQuantity(value);
    }
  };

  const handleAddToCart = async () => {
    try {
      if (isLoggedIn()) {
        const memberId = getMemberId();
        if (!memberId) throw new Error('로그인 정보가 없습니다');
        await addToCart(book.id, memberId, 1);
      } else {
        await addToCart(book.id, 1, 1);
      }
    } catch (error) {
      console.error('장바구니 추가 실패', error);
      alert(error instanceof Error ? error.message : '장바구니 추가에 실패했습니다');
    }
  };

  const averageRating =
    book.reviewCount > 0 ? (book.rating / book.reviewCount).toFixed(1) : '평점 없음';

  const totalPrice = book.priceSales * quantity;

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
          <div className="flex items-center gap-4 mb-4">
            <div className="flex items-center gap-2">
              <label htmlFor="quantity" className="text-sm font-medium">
                수량
              </label>
              <input
                id="quantity"
                type="number"
                min="1"
                value={quantity}
                onChange={handleQuantityChange}
                className="w-20 px-2 py-1 border rounded text-center"
              />
            </div>
            <div className="flex-1 text-right">
              <span className="text-sm text-gray-600">총 상품금액</span>
              <p className="text-lg font-bold text-red-600">{totalPrice.toLocaleString()}원</p>
            </div>
          </div>
          <div className="flex flex-col gap-2">
            <AddToCartButton
              bookId={book.id}
              memberId={isLoggedIn() ? Number(getMemberId()) : 0}
              quantity={quantity}
              className="w-full py-3"
            />
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
