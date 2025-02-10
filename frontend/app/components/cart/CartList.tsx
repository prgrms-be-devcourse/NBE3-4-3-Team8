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

interface CartItemData {
  bookId: number;
  title: string;
  quantity: number;
  price: number;
  coverImage: string;
}

const CartList = () => {
  const [items, setItems] = useState<CartItemData[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const loadCart = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const cartData = await fetchCart();
      setItems(cartData);
      setIsLoggedIn(true);
    } catch (error) {
      console.error('장바구니 불러오기 실패:', error);
      if (error.response && error.response.status === 401) {
        setIsLoggedIn(false);
        await loadLocalCart();
      } else {
        setError('장바구니를 불러오는 중 오류가 발생했습니다.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const loadLocalCart = async () => {
    const localCart = localStorage.getItem('localCart');
    if (localCart) {
      try {
        const parsedLocalCart = JSON.parse(localCart);
        const cartData = await fetchAnonymousCart(parsedLocalCart);
        setItems(cartData);
      } catch (error) {
        console.error('익명 장바구니 불러오기 실패:', error);
        if (error.response) {
          setError(`서버 오류: ${error.response.status} - ${error.response.data.message}`);
        } else if (error.request) {
          setError('서버에 연결할 수 없습니다. 네트워크 연결을 확인해주세요.');
        } else {
          setError(`오류 발생: ${error.message}`);
        }
      }
    } else {
      setItems([]);
    }
  };

  const syncLocalCartWithDB = async () => {
    const localCart = localStorage.getItem('localCart');
    if (localCart) {
      try {
        const parsedLocalCart = JSON.parse(localCart);
        await addToCart(parsedLocalCart);
        localStorage.removeItem('localCart');
        await loadCart();
      } catch (error) {
        console.error('로컬 장바구니 동기화 실패:', error);
      }
    }
  };

  useEffect(() => {
    loadCart();
  }, []);

  useEffect(() => {
    if (isLoggedIn) {
      syncLocalCartWithDB();
    }
  }, [isLoggedIn]);

  const handleQuantityChange = async (bookId: number, newQuantity: number) => {
    try {
      await updateCartItem([{ bookId, quantity: newQuantity, isAddToCart: false }]);
      await loadCart();
    } catch (error) {
      console.error('수량 변경 실패:', error);
      if (error.response && error.response.status === 401) {
        const updatedItems = items.map((item) =>
          item.bookId === bookId ? { ...item, quantity: newQuantity } : item,
        );
        setItems(updatedItems);
        localStorage.setItem('localCart', JSON.stringify(updatedItems));
      }
    }
  };

  const handleRemove = async (bookId: number) => {
    try {
      await removeCartItems([{ bookId, quantity: 1 }]);
      await loadCart();
    } catch (error) {
      console.error('장바구니 삭제 실패:', error);
      if (error.response && error.response.status === 401) {
        const updatedItems = items.filter((item) => item.bookId !== bookId);
        setItems(updatedItems);
        localStorage.setItem('localCart', JSON.stringify(updatedItems));
      }
    }
  };

  const totalPrice = items.reduce((total, item) => total + item.price * item.quantity, 0);

  if (isLoading) return <div>로딩 중...</div>;
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
            <div className="space-y-2 mb-4">
              <div className="flex justify-between">
                <span>배송비</span>
                <span>무료</span>
              </div>
              <div className="flex justify-between">
                <span>상품 할인</span>
                <span>확인 필요</span>
              </div>
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
