'use client';
import React, { useEffect, useState } from 'react';
import Image from 'next/image';

interface BookDetailsProps {
    bookId: number;
}

interface BookDetailData {
    description: string;
    descriptionImage: string;
    toc: string;
}

export const BookDetails: React.FC<BookDetailsProps> = ({ bookId }) => {
    if (!bookId) {
        return <div className="text-center text-gray-500">도서 정보가 없습니다.</div>;
    }

    const [bookDetail, setBookDetail] = useState<BookDetailData | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchBookDetail = async () => {
            try {
                setLoading(true);
                const response = await fetch(`http://localhost:8080/books/${bookId}`, {
                    cache: 'no-store',
                });
                if (!response.ok) {
                    throw new Error('도서 상세 정보를 불러오지 못했습니다.');
                }
                const data: BookDetailData = await response.json();
                setBookDetail(data);
                setError(null);
            } catch (err) {
                setError(err instanceof Error ? err.message : '오류 발생');
            } finally {
                setLoading(false);
            }
        };

        fetchBookDetail();
    }, [bookId]);

    if (loading) return <div className="text-center">로딩 중...</div>;
    if (error) return <div className="text-center text-red-500">{error}</div>;
    if (!bookDetail) return <div className="text-center">도서 정보가 없습니다.</div>;

    return (
        <div className="p-4 space-y-8">
            {/* 설명 영역 */}
            <div className="border rounded-lg p-6 shadow-sm">
                <h2 className="text-center text-2xl font-bold mb-4">설명</h2>
                {bookDetail.description ? (
                    <p className="text-center text-lg leading-relaxed">
                        {bookDetail.description}
                    </p>
                ) : (
                    <p className="text-center text-gray-500">설명이 없습니다.</p>
                )}
            </div>

            {/* 이미지 영역 */}
            <div className="border rounded-lg p-6 shadow-sm">
                <h2 className="text-center text-2xl font-bold mb-4">이미지</h2>
                <div className="flex justify-center">
                    {bookDetail.descriptionImage ? (
                        <Image
                            src={bookDetail.descriptionImage}
                            alt="도서 상세 이미지"
                            width={300}
                            height={300}
                            className="object-contain"
                        />
                    ) : (
                        <p className="text-center text-gray-500">커버 이미지가 없습니다.</p>
                    )}
                </div>
            </div>

            {/* 목차 영역 */}
            <div className="border rounded-lg p-6 shadow-sm">
                <h2 className="text-center text-2xl font-bold mb-4 ">목차</h2>
                {bookDetail.toc ? (
                    <p className="text-center text-lg leading-relaxed">
                        {bookDetail.toc}
                    </p>
                ) : (
                    <p className="text-center text-gray-500">목차가 없습니다.</p>
                )}
            </div>
        </div>
    );
};