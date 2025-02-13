'use client';

import React, { useState, useEffect } from 'react';
import CartItem from './CartItem';
import {
  fetchCart,
  updateCartItem,
  removeCartItems,
  fetchAnonymousCart,
  addToCart,
} from '@/utils/api.js';
import { useAuth } from '@/app/hooks/useAuth';
import { useRouter } from 'next/navigation';

interface CartItemData {
  bookId: number;
  title: string;
  quantity: number;
  price: number;
  coverImage: string;
}

const CartList = () => {
  const { user, loading: authLoading } = useAuth();
  const router = useRouter();
  const [items, setItems] = useState<CartItemData[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // 장바구니 불러오기
  const loadCart = async () => {
    setIsLoading(true);
    setError(null);

    try {
      if (user) {
        // 로그인 상태: DB에서 장바구니 데이터 불러오기
        const cartData = await fetchCart();
        setItems(cartData);
      } else {
        // 비로그인 상태: 로컬 스토리지에서 장바구니 데이터 불러오기
        await loadLocalCart();
      }
    } catch (error) {
      console.error('장바구니 불러오기 실패:', error);
      setError('장바구니를 불러오는 중 오류가 발생했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  // 로컬 스토리지에서 장바구니 데이터 불러오기
  const loadLocalCart = async () => {
    const localCart = localStorage.getItem('localCart');
    if (localCart) {
      try {
        const parsedLocalCart = JSON.parse(localCart);
        const cartData = await fetchAnonymousCart(parsedLocalCart);
        setItems(cartData);
      } catch (error) {
        console.error('익명 장바구니 불러오기 실패:', error);
        setError('로컬 장바구니 데이터를 불러오는 중 오류가 발생했습니다.');
      }
    } else {
      setItems([]);
    }
  };

  // 로컬 스토리지와 DB 동기화 (로그인 시)
  const syncLocalCartWithDB = async () => {
    if (!user) return;

    const localCart = localStorage.getItem('localCart');
    if (localCart) {
      try {
        const parsedLocalCart = JSON.parse(localCart);
        // 서버 업데이트를 위한 플래그 설정 (필요에 따라 값 조정)
        parsedLocalCart.isAddToCart = true;
        localStorage.removeItem('localCart');
        await updateCartItem(parsedLocalCart);
        await loadCart();
      } catch (error) {
        console.error('로컬 장바구니 동기화 실패:', error);
      }
    }
  };

  // 수량 변경 처리
  const handleQuantityChange = async (bookId: number, newQuantity: number) => {
    if (newQuantity < 1) return;
    try {
      if (user) {
        await updateCartItem([{ bookId, quantity: newQuantity, isAddToCart: false }]);
        await loadCart();
      } else {
        const updatedItems = items.map((item) =>
          item.bookId === bookId ? { ...item, quantity: newQuantity } : item,
        );
        setItems(updatedItems);
        localStorage.setItem('localCart', JSON.stringify(updatedItems));
      }
    } catch (error) {
      console.error('수량 변경 실패:', error);
      setError('수량 변경 중 오류가 발생했습니다.');
    }
  };

  // 항목 삭제 처리
  const handleRemove = async (bookId: number) => {
    try {
      if (user) {
        await removeCartItems([{ bookId, quantity: 1 }]);
        await loadCart();
      } else {
        const updatedItems = items.filter((item) => item.bookId !== bookId);
        setItems(updatedItems);
        localStorage.setItem('localCart', JSON.stringify(updatedItems));
      }
    } catch (error) {
      console.error('장바구니 삭제 실패:', error);
      setError('장바구니 항목 삭제 중 오류가 발생했습니다.');
    }
  };

  // "구매하기" 버튼 클릭 시 주문 페이지로 이동
  const handlePurchase = () => {
    if (user) {
      router.push('/order');
    } else {
      alert('로그인 후 이용해주세요.');
    }
  };

  useEffect(() => {
    if (!authLoading) loadCart();
  }, [authLoading]);

  useEffect(() => {
    if (user) syncLocalCartWithDB();
  }, [user]);

  const totalPrice = items.reduce((total, item) => total + item.price * item.quantity, 0);

  if (isLoading || authLoading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="max-w-6xl mx-auto py-8">
      <h1 className="text-2xl font-bold mb-6">장바구니 ({items.length})</h1>
      {items.length === 0 ? (
        <div className="text-center py-10">장바구니가 비어 있습니다.</div>
      ) : (
        <div className="flex flex-col md:flex-row gap-10">
          {/* 좌측: 장바구니 상품 목록 */}
          <div className="flex-1 space-y-6 max-h-[80vh] overflow-y-auto">
            {items.map((item) => (
              <CartItem
                key={item.bookId}
                title={item.title}
                quantity={item.quantity}
                price={item.price}
                coverImage={item.coverImage}
                onQuantityChange={(newQuantity) => handleQuantityChange(item.bookId, newQuantity)}
                onRemove={() => handleRemove(item.bookId)}
              />
            ))}
          </div>
          {/* 우측: 결제 요약 및 구매하기 버튼 */}
          <div className="w-full md:w-96 border border-gray-200 rounded p-6">
            <h2 className="text-lg font-medium mb-4">상품 금액</h2>
            <div className="flex justify-between mb-4">
              <span>총 상품 금액</span>
              <span>{totalPrice.toLocaleString()}원</span>
            </div>
            <button
              onClick={handlePurchase}
              className="w-full bg-black text-white py-3 rounded hover:bg-gray-800 transition-colors"
            >
              구매하기
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default CartList;