// CartList.tsx
"use client";
import React, { useState, useEffect } from "react";
import CartItem from "./CartItem";
import { fetchCart, updateCartItem, removeCartItems } from "@/utils/api.js";

interface CartItemData {
    member: number; // memberId
    id: number; // bookId
    title: string;
    quantity: number;
    price: number; // 가격 필드 추가
    coverImage: string;
}

const CartList = () => {
    const [items, setItems] = useState<CartItemData[]>([]);
    // 현재는 memberId가 1번으로 고정되어 있습니다.
    const memberId = 1;

    // 장바구니 목록 불러오기
    const loadCart = async () => {
        try {
            const cartData = await fetchCart(memberId);
            console.log("📌 장바구니 데이터:", cartData);

            const newItems = cartData.map((cartItem: any) => ({
                member: cartItem.memberId,
                id: cartItem.bookId,
                title: cartItem.title,
                quantity: cartItem.quantity,
                price: cartItem.price,
                coverImage: cartItem.coverImage || "/default-book.png",
            }));

            setItems(newItems);
        } catch (error) {
            console.error("장바구니 불러오기 실패:", error);
        }
    };

    useEffect(() => {
        loadCart();
    }, []);

    // 수량 변경
    const handleQuantityChange = async (bookId: number, newQuantity: number) => {
        try {
            await updateCartItem(bookId, memberId, newQuantity);
            await loadCart();
        } catch (error) {
            console.error("수량 변경 실패:", error);
        }
    };

    // 장바구니 아이템 삭제
    const handleRemove = async (bookId: number) => {
        try {
            await removeCartItems(memberId, [{ bookId, quantity: 1 }]);
            await loadCart();
        } catch (error) {
            console.error("장바구니 삭제 실패:", error);
        }
    };

    // 총 상품 금액 계산 (각 상품의 price * quantity 합산)
    const totalPrice = items.reduce((total, item) => total + item.price * item.quantity, 0);

    return (
        <div className="max-w-6xl mx-auto py-8">
            <h1 className="text-2xl font-bold mb-6">장바구니 ({items.length})</h1>

            <div className="flex gap-10">
                {/* 장바구니 아이템 목록 영역에 내부 스크롤 및 반응형 높이 적용 */}
                <div className="flex-1 space-y-6 max-h-[80vh] overflow-y-auto">
                    {items.map((item) => (
                        <CartItem
                            key={item.id}
                            title={item.title}
                            quantity={item.quantity}
                            price={item.price}
                            coverImage={item.coverImage}
                            onQuantityChange={(newQuantity) =>
                                handleQuantityChange(item.id, newQuantity)
                            }
                            onRemove={() => handleRemove(item.id)}
                        />
                    ))}
                </div>

                {/* 장바구니 요약 영역 */}
                <div className="w-96 border border-gray-200 rounded p-6">
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
                    <button className="w-full bg-black text-white py-3 rounded">
                        구매하기
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CartList;
