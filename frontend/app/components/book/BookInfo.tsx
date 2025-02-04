"use client";
import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Image from "next/image"; // Next.js 이미지 컴포넌트
import { fetchBookById, addToCart } from "@/utils/api.js";

interface BookInfoProps {
    bookId: string;
}

export const BookInfo: React.FC<BookInfoProps> = ({ bookId }) => {
    const router = useRouter();
    const [book, setBook] = useState<any>(null);

    useEffect(() => {
        const loadBook = async () => {
            try {
                const bookData = await fetchBookById(bookId);
                console.log("📌 가져온 도서 데이터:", bookData);

                // 백엔드에서 가져온 데이터를 변환하여 저장
                const formattedBook = {
                    ...bookData,
                    originalPrice: bookData.price,
                    salePrice: bookData.price,
                    rating:
                        bookData.reviewCount > 0
                            ? (bookData.rating / bookData.reviewCount).toFixed(1)
                            : "N/A",
                };

                setBook(formattedBook);
            } catch (error) {
                console.error("도서 정보를 불러오지 못했습니다.");
            }
        };
        loadBook();
    }, [bookId]);

    if (!book) return <p>📌 도서 정보를 불러오는 중...</p>;

    const handleAddToCart = async () => {
        try {
            await addToCart(book.id, 1, 1); // TODO bookid, memberid, quantity인데 member 값 보고 수정해야댐
            router.push("/cart");
        } catch (error) {
            console.error("장바구니 추가 실패");
        }
    };

    return (
        <div className="flex gap-8 my-8">
            {/* 책 이미지 영역 */}
            <div className="w-80 h-96 border border-black flex items-center justify-center bg-gray-100 relative">
                <Image
                    src={book.coverImage ?? "/default-book.png"}
                    alt={book.title}
                    fill
                    style={{ objectFit: "cover" }}
                    sizes="(max-width: 768px) 100vw,
                 (max-width: 1024px) 50vw,
                 33vw"
                    priority
                />
            </div>

            {/* 책 정보 */}
            <div className="flex-1">
                <h1 className="text-2xl font-bold mb-2">{book.title}</h1>
                <p className="text-sm text-gray-600">
                    {book.author} &gt; {book.publisher || "출판사 미정"} &gt; 소설
                </p>

                {/* 가격 정보 */}
                <div className="mt-4">
                    <p>정가: {book.originalPrice.toLocaleString()}원</p>
                    <p>판매가: {book.salePrice.toLocaleString()}원</p>
                    <p>배송료: 무료</p>
                    <p>
                        평점: {book.rating}점 리뷰({book.reviewCount})
                    </p>
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
                        onClick={() => router.push("/cart")}
                    >
                        바로구매
                    </button>
                </div>
            </div>
        </div>
    );
};
