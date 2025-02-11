'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import type { Book } from '@/types/book';
import { addToCart } from '@/utils/api';
import { AddToCartButton } from '@/app/components/common/AddToCartButton';
import StarRating from '@/app/search/components/StarRating';
import { useAuth } from '@/app/hooks/useAuth'; // useAuth 훅 가져오기

interface BookInfoProps {
  book: Book;
}

export const BookInfo: React.FC<BookInfoProps> = ({ book }) => {
  const [quantity, setQuantity] = useState(1);
  const router = useRouter();
  const { user, loading } = useAuth(); // useAuth 훅 사용하여 로그인 상태 확인

  const handleQuantityChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = parseInt(e.target.value);
    if (value > 0) {
      setQuantity(value);
    }
  };

  const handleAddToCart = async () => {
    try {
      if (loading) {
        alert('로그인 상태를 확인 중입니다. 잠시만 기다려주세요.');
        return;
      }

      if (user) {
        // 로그인된 사용자일 경우
        await addToCart([{ bookId: book.id, quantity, isAddToCart: true }]); // 서버에 장바구니 추가 요청
        alert('장바구니에 추가되었습니다.');
      } else {
        // 비로그인 사용자일 경우 로컬 스토리지에 저장
        const localCart = localStorage.getItem('localCart');
        const cartItems = localCart ? JSON.parse(localCart) : [];

        // 동일한 bookId가 있는지 확인
        const existingItemIndex = cartItems.findIndex(
          (item: { bookId: number }) => item.bookId === book.id,
        );

        if (existingItemIndex > -1) {
          // 이미 존재하는 항목이면 수량 업데이트
          cartItems[existingItemIndex].quantity += quantity;
        } else {
          // 새 항목 추가
          cartItems.push({ bookId: book.id, quantity });
        }

        localStorage.setItem('localCart', JSON.stringify(cartItems));
        alert('장바구니에 추가되었습니다.');
      }
    } catch (error) {
      alert(error instanceof Error ? error.message : '장바구니 추가에 실패했습니다.');
    }
  };

  const averageRating =
    book.reviewCount > 0 ? (book.rating / book.reviewCount).toFixed(1) : '평점 없음';

  const totalPrice = book.priceSales * quantity;

  return (
    <div className="max-w-[800px] mx-auto px-4 py-8">
      <div className="flex gap-8">
        <div className="w-[380px] flex-shrink-0">
          <Image
            src={book.coverImage || '/default-book.png'}
            alt={book.title}
            width={380}
            height={500}
            className="w-full h-auto object-contain"
            priority
          />
        </div>
        <div className="w-[380px]">
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
            <StarRating rating={book.averageRating ? book.averageRating : 0} />
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
            <button
              onClick={handleAddToCart}
              className="w-full py-3 bg-blue-500 text-white rounded-md hover:bg-blue-600"
            >
              장바구니 추가
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
