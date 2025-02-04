// CartItem.tsx
import React from "react";
import Image from "next/image";

interface CartItemProps {
    title: string;
    quantity: number;
    price: number; // 가격 추가
    coverImage: string;
    onQuantityChange: (newQuantity: number) => void;
    onRemove: () => void;
}

const CartItem: React.FC<CartItemProps> = ({
                                               title,
                                               quantity,
                                               price,
                                               coverImage,
                                               onQuantityChange,
                                               onRemove,
                                           }) => {
    return (
        <div className="flex items-center gap-6 p-6 border border-gray-200 rounded-lg w-full">
            {/* 이미지 영역 */}
            <div className="w-32 h-32 bg-gray-100 relative">
                <Image
                    src={coverImage}
                    alt={title}
                    fill
                    className="rounded-lg object-cover"
                />
            </div>

            {/* 도서 정보 */}
            <div className="flex-1">
                <h3 className="text-lg font-medium">{title}</h3>
                <p className="text-gray-500">가격: {price.toLocaleString()}원</p>
                <div className="flex items-center gap-4 mt-2">
                    <button
                        onClick={() => quantity > 1 && onQuantityChange(quantity - 1)}
                        className="w-10 h-10 border border-gray-300 rounded flex items-center justify-center"
                    >
                        -
                    </button>
                    <span className="w-10 text-center text-lg">{quantity}</span>
                    <button
                        onClick={() => onQuantityChange(quantity + 1)}
                        className="w-10 h-10 border border-gray-300 rounded flex items-center justify-center"
                    >
                        +
                    </button>
                </div>
            </div>

            {/* 삭제 버튼 */}
            <button onClick={onRemove} className="text-gray-500 hover:text-gray-700 text-2xl">
                ✕
            </button>
        </div>
    );
};

export default CartItem;
