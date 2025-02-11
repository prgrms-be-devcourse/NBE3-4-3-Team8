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

interface CartItemData {
  bookId: number;
  title: string;
  quantity: number;
  price: number;
  coverImage: string;
}

const CartList = () => {
  const { user, loading: authLoading } = useAuth(); // useAuth 훅 사용
  const [items, setItems] = useState<CartItemData[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // 장바구니 불러오기
  const loadCart = async () => {
    setIsLoading(true);
    setError(null);

    try {
      if (user) {
        // 로그인 상태에서 DB에서 장바구니 데이터 불러오기
        const cartData = await fetchCart();
        setItems(cartData);
      } else {
        // 비로그인 상태에서 로컬 스토리지 장바구니 데이터 불러오기
        await loadLocalCart();
      }
    } catch (error) {
      console.error('장바구니 불러오기 실패:', error);
      setError('장바구니를 불러오는 중 오류가 발생했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  // 로컬 스토리지에서 장바구니 데이터를 불러오는 함수
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

  // 로컬 스토리지와 DB 동기화
  const syncLocalCartWithDB = async () => {
    if (!user) return; // 로그인하지 않은 경우 동기화하지 않음

    const localCart = localStorage.getItem('localCart');
    if (localCart) {
      try {
        const parsedLocalCart = JSON.parse(localCart);
        parsedLocalCart.isAddToCart = true;
        // 로컬 스토리지 초기화
        localStorage.removeItem('localCart');

        // 로컬 스토리지 데이터를 서버에 업데이트
        await updateCartItem(parsedLocalCart);

        // 최신 데이터 다시 불러오기
        await loadCart();
      } catch (error) {
        console.error('로컬 장바구니 동기화 실패:', error);
      }
    }
  };

  // 수량 변경 처리
  const handleQuantityChange = async (bookId: number, newQuantity: number) => {
    try {
      if (user) {
        // 로그인 상태에서는 DB 업데이트
        await updateCartItem([{ bookId, quantity: newQuantity, isAddToCart: false }]);
        await loadCart();
      } else {
        // 비로그인 상태에서는 로컬 스토리지 업데이트
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
        // 로그인 상태에서는 DB에서 삭제
        await removeCartItems([{ bookId, quantity: 1 }]);
        await loadCart();
      } else {
        // 비로그인 상태에서는 로컬 스토리지에서 삭제
        const updatedItems = items.filter((item) => item.bookId !== bookId);
        setItems(updatedItems);
        localStorage.setItem('localCart', JSON.stringify(updatedItems));
      }
    } catch (error) {
      console.error('장바구니 삭제 실패:', error);
      setError('장바구니 항목 삭제 중 오류가 발생했습니다.');
    }
  };

  useEffect(() => {
    if (!authLoading) loadCart(); // 인증 상태 확인 후 장바구니 불러오기
  }, [authLoading]);

  useEffect(() => {
    if (user) syncLocalCartWithDB(); // 로그인 시 로컬 장바구니와 동기화
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

          <div className="w-full md:w-96 border border-gray-200 rounded p-6">
            <h2 className="text-lg font-medium mb-4">상품 금액</h2>
            <div className="flex justify-between mb-4">
              <span>총 상품 금액</span>
              <span>{totalPrice.toLocaleString()}원</span>
            </div>
            <button className="w-full bg-black text-white py-3 rounded hover:bg-gray-800 transition-colors">
              구매하기
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default CartList;
