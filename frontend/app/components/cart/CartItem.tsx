import React from 'react';
import Image from 'next/image';

interface CartItemProps {
    title: string;
    quantity: number;
    onQuantityChange: (newQuantity: number) => void;
    onRemove: () => void;
}

const CartItem: React.FC<CartItemProps> = ({
                                               title,
                                               quantity,
                                               onQuantityChange,
                                               onRemove,
                                           }) => {
    return (
        <div className="flex items-center gap-4 p-4 border border-gray-200 rounded">
            <div className="w-24 h-24 bg-gray-100 relative">
                <Image
                    src="/api/placeholder/96/96"
                    alt={title}
                    layout="fill"
                    objectFit="cover"
                />
            </div>
            <div className="flex-1">
                <h3 className="text-lg font-medium">{title}</h3>
                <div className="flex items-center gap-2 mt-2">
                    <button
                        onClick={() => quantity > 1 && onQuantityChange(quantity - 1)}
                        className="w-8 h-8 border border-gray-300 rounded flex items-center justify-center"
                    >
                        -
                    </button>
                    <span className="w-8 text-center">{quantity}</span>
                    <button
                        onClick={() => onQuantityChange(quantity + 1)}
                        className="w-8 h-8 border border-gray-300 rounded flex items-center justify-center"
                    >
                        +
                    </button>
                </div>
            </div>
            <button
                onClick={onRemove}
                className="text-gray-500 hover:text-gray-700"
            >
                ✕
            </button>
        </div>
    );
};

export default CartItem;