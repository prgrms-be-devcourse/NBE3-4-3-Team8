"use client";
import React from "react";
import { useRouter } from "next/navigation";

interface BookInfoProps {
    title: string;
    author: string;
    publisher: string;
    originalPrice: number;
    salePrice: number;
    rating: number;
    reviewCount: number;
}

export const BookInfo: React.FC<BookInfoProps> = ({
                                                      title,
                                                      author,
                                                      publisher,
                                                      originalPrice,
                                                      salePrice,
                                                      rating,
                                                      reviewCount,
                                                  }) => {
    const router = useRouter();

    // 장바구니 버튼 클릭 시
    const handleAddToCart = () => {
        router.push("/cart");
    };

    // 바로구매 버튼 클릭 시
    const handlePurchase = () => {
        router.push("/cart");
    };

    return (
        <div className="flex gap-8 my-8">
            {/* 책 이미지 영역 */}
            <div className="w-80 h-96 border border-black flex items-center justify-center bg-gray-100">
                Book Image
            </div>

            {/* 책 정보 영역 */}
            <div className="flex-1">
                <div className="border-b border-black pb-4">
                    <h1 className="text-2xl font-bold mb-2">{title}</h1>
                    <p className="text-sm text-gray-600">
                        {author} &gt; {publisher} &gt; 소설
                    </p>
                </div>

                {/* 가격, 평점, 수량 선택 등 */}
                <div className="mt-4">
                    <p>정가: {originalPrice.toLocaleString()}원</p>
                    <p>
                        판매가: {salePrice.toLocaleString()}원 (
                        {(((originalPrice - salePrice) / originalPrice) * 100).toFixed(0)}%
                        할인)
                    </p>
                    <p>배송료: 무료</p>
                    <p>
                        평점: {rating}점 &nbsp; 리뷰({reviewCount})
                    </p>
                    {/* 수량 선택 UI는 상황에 맞춰 추가 */}
                </div>

                {/* 장바구니 담기 / 바로구매 버튼 */}
                <div className="flex gap-4 mt-6">
                    <button
                        className="px-4 py-2 bg-gray-200 border border-gray-600"
                        onClick={handleAddToCart}
                    >
                        장바구니 담기
                    </button>
                    <button
                        className="px-4 py-2 bg-gray-200 border border-gray-600"
                        onClick={handlePurchase}
                    >
                        바로구매
                    </button>
                </div>
            </div>
        </div>
    );
};
