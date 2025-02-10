// components/common/AddToCartButton.tsx
'use client';
import { useState } from 'react';
import { addToCart } from '@/utils/cart';

interface AddToCartButtonProps {
  bookId: number;
  jwtToken: string | null;
  quantity: number;
  className?: string;
}

export const AddToCartButton = ({
  bookId,
  jwtToken,
  quantity,
  className,
}: AddToCartButtonProps) => {
  const [isAdding, setIsAdding] = useState(false);

  const handleClick = async () => {
    setIsAdding(true);
    try {
      await addToCart(bookId, quantity);
      alert('장바구니에 추가되었습니다');
    } catch (error) {
      console.error('장바구니 추가 실패:', error);
      alert('장바구니 추가에 실패했습니다');
    } finally {
      setIsAdding(false);
    }
  };

  return (
    <button
      onClick={handleClick}
      disabled={isAdding}
      className={`${className} w-full py-3 bg-blue-500 text-white rounded-md ${
        isAdding ? 'opacity-50 cursor-not-allowed' : 'hover:bg-blue-600'
      }`}
    >
      {isAdding ? '추가 중...' : '장바구니 담기'}
    </button>
  );
};
