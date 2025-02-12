'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import Image from 'next/image';
import type { Book } from '@/types/book';
import { addToCart } from '@/utils/api';
import StarRating from '@/app/search/components/StarRating';
import { useAuth } from '@/app/hooks/useAuth';

interface BookInfoProps {
  book: Book;
}

export const BookInfo: React.FC<BookInfoProps> = ({ book }) => {
  const [quantity, setQuantity] = useState(1);
  const router = useRouter();
  const { user, loading } = useAuth();

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
        await addToCart([{ bookId: book.id, quantity, isAddToCart: true }]);
        alert('장바구니에 추가되었습니다.');
      } else {
        const localCart = localStorage.getItem('localCart');
        const cartItems = localCart ? JSON.parse(localCart) : [];
        const existingItemIndex = cartItems.findIndex(
          (item: { bookId: number }) => item.bookId === book.id,
        );
        if (existingItemIndex > -1) {
          cartItems[existingItemIndex].quantity += quantity;
        } else {
          cartItems.push({ bookId: book.id, quantity });
        }
        localStorage.setItem('localCart', JSON.stringify(cartItems));
        alert('장바구니에 추가되었습니다.');
      }
    } catch (error) {
      alert(error instanceof Error ? error.message : '장바구니 추가에 실패했습니다.');
    }
  };

  // "바로 구매" 버튼 클릭 시 현재 책 정보를 URL 파라미터로 전달하며 주문 페이지로 이동
  const handleFastPurchase = () => {
    if (user) {
      const orderItem = {
        bookId: book.id,
        title: book.title,
        coverImage: book.coverImage || '/default-book.png',
        price: book.priceSales, // 판매가 사용
        quantity,
      };
      const orderItemStr = encodeURIComponent(JSON.stringify(orderItem));
      router.push(`/order?fastOrder=true&orderItem=${orderItemStr}`);
    } else {
      alert('로그인 후 이용해주세요.');
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
            {/* 기존에는 장바구니 페이지로 이동했지만, 이제 바로 주문 페이지로 이동 */}
            <button
              className="w-full py-3 bg-green-500 text-white rounded-md hover:bg-green-600"
              onClick={handleFastPurchase}
            >
              바로구매
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};