import React from 'react';
import BookList from '../components/BookList';

export default function SearchPage() {
    // 예시용 하드코딩된 데이터
    const dummyBooks = [
        {
            id: 1,
            title: '도서 제목 예시',
            author: '저자',
            publisher: '출판사',
            originalPrice: 50000,
            salePrice: 45000,
            rating: 0.0,
            reviewCount: 0,
        },
        {
            id: 2,
            title: '다른 도서 제목',
            author: '저자',
            publisher: '출판사',
            originalPrice: 20000,
            salePrice: 18000,
            rating: 4.5,
            reviewCount: 12,
        },
        {
            id: 3,
            title: '또 다른 도서',
            author: '저자',
            publisher: '출판사',
            originalPrice: 30000,
            salePrice: 27000,
            rating: 3.8,
            reviewCount: 5,
        },
    ];

    return (
        <div className="py-8">
            <h2 className="text-xl font-bold mb-6">
                &apos;검색어&apos;에 대한 {dummyBooks.length}개의 검색결과
            </h2>

            {/* 정렬/필터/페이지네이션 등을 위한 영역 (간단히 예시) */}
            <div className="flex gap-4 mb-4">
                <select className="border border-gray-300 rounded px-2 py-1">
                    <option>인기순</option>
                    <option>평점순</option>
                    <option>최신순</option>
                </select>
                <select className="border border-gray-300 rounded px-2 py-1">
                    <option>20개씩 보기</option>
                    <option>40개씩 보기</option>
                </select>
            </div>

            {/* 실제 도서 목록 */}
            <BookList books={dummyBooks} />

            {/* 페이지네이션 UI 예시 */}
            <div className="flex justify-center mt-8 gap-2">
                <button className="px-3 py-1 border border-gray-300 rounded">1</button>
                <button className="px-3 py-1 border border-gray-300 rounded">2</button>
                <button className="px-3 py-1 border border-gray-300 rounded">3</button>
                {/* ... */}
                <button className="px-3 py-1 border border-gray-300 rounded">10</button>
            </div>
        </div>
    );
}
