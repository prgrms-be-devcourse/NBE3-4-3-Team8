"use client";
import React from "react";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { Book } from "@/types/book";
import StarRating from "./StarRating";

const BookCard: React.FC<{ book: Book }> = ({ book }) => {
    const router = useRouter();

    const handleClick = () => {
        router.push(`/books/${book.id}`);
    };

    return (
        <div
            onClick={handleClick}
            className="border rounded-lg p-4 shadow-sm hover:shadow-lg transition-shadow bg-white cursor-pointer w-[500px] md:w-[540px] lg:w-[580px]"
        >
            {/* 이미지 영역: 가로 비율을 1.5배 넓게 하기 위해 aspect-[3/2] 적용 */}
            <div className="relative w-full aspect-[3/2] mb-4">
                <Image
                    src={book.coverImage || "/default-book.png"}
                    alt={book.title}
                    fill
                    className="object-contain rounded"
                />
            </div>
            <h2 className="text-lg font-semibold mb-1">{book.title}</h2>
            <p className="text-sm text-gray-700 mb-1">저자: {book.author}</p>
            <p className="text-sm text-gray-700 mb-1">출판사: {book.publisher}</p>
            <p className="text-sm text-gray-500 line-through">
                정가: {book.priceStandard.toLocaleString()}원
            </p>
            <p className="text-lg font-bold text-red-600 mb-1">
                판매가: {book.priceSales.toLocaleString()}원
            </p>
            {/* 평균 평점 및 리뷰 개수 표시 */}
            <p className="text-sm">
                <span className="text-gray-500">평점: </span>
                <span className="text-yellow-500">
                    {book.averageRating ? book.averageRating.toFixed(1) : "평점 없음"}
                </span>
                <span className="text-gray-500"> ({book.reviewCount})</span>
            </p>
            {/* 별점 아이콘 표시 */}
            <div>
                <StarRating rating={book.averageRating ? book.averageRating : 0}/>
            </div>
            <p className="text-sm mt-2 text-gray-600">
                {book.description || "설명 없음"}
            </p>
        </div>
    );
};

export default BookCard;