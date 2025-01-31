"use client";
import React, { useState } from 'react';
import CartItem from './CartItem';

interface CartItem {
    id: number;
    title: string;
    quantity: number;
}

const CartList = () => {
    const [items, setItems] = useState<CartItem[]>([
        { id: 1, title: "구매 도서 제목", quantity: 1 },
        { id: 2, title: "구매 도서 제목", quantity: 1 },
    ]);

    const handleQuantityChange = (id: number, newQuantity: number) => {
        setItems(items.map(item =>
            item.id === id ? { ...item, quantity: newQuantity } : item
        ));
    };

    const handleRemove = (id: number) => {
        setItems(items.filter(item => item.id !== id));
    };

    return (
        <div className="max-w-4xl mx-auto py-8">
            <h1 className="text-2xl font-bold mb-6">장바구니(2)</h1>

            <div className="flex gap-6">
                <div className="flex-1">
                    <div className="space-y-4">
                        {items.map((item) => (
                            <CartItem
                                key={item.id}
                                title={item.title}
                                quantity={item.quantity}
                                onQuantityChange={(newQuantity) => handleQuantityChange(item.id, newQuantity)}
                                onRemove={() => handleRemove(item.id)}
                            />
                        ))}
                    </div>
                </div>

                <div className="w-80">
                    <div className="border border-gray-200 rounded p-4">
                        <h2 className="text-lg font-medium mb-4">상품 금액</h2>
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
                        <button className="w-full bg-black text-white py-2 rounded">
                            구매하기
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CartList;