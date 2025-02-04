import React from 'react';
import { BookInfo } from '@/app/components/book/BookInfo';
import { BookTabs } from '@/app/components/book/BookTabs';

interface BookDetailPageProps {
    params: {
        id: string;
    };
}

export default function BookDetailPage({ params }: BookDetailPageProps) {
    // 실제로는 이 부분에서 params.id를 사용하여 책 데이터를 가져옵니다
    const bookData = {
        title: "구체적인 도서 제목",
        author: "작가명",
        publisher: "출판사",
        originalPrice: 50000,
        salePrice: 45000,
        rating: 0,
        reviewCount: 0
    };

    return (
        <div className="min-h-screen bg-white">
            <BookInfo {...bookData} />
            <BookTabs />
        </div>
    );
}