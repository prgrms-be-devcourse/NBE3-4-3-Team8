'use client';
import React from 'react';
import Image from 'next/image';
import { Book } from '@/types/book';

interface SearchResultItemProps {
  book: Book;
}

const SearchResultItem: React.FC<SearchResultItemProps> = ({ book }) => {
  return (
    <div className="flex items-center gap-6 p-6 border border-gray-200 rounded-lg w-full">
      {/* 책 이미지 영역 */}
      <div className="w-32 h-32 bg-gray-100 relative">
        <Image
          src={book.coverImage || '/default-book.png'}
          alt={book.title}
          fill
          className="object-cover rounded-lg"
        />
      </div>

      {/* 책 정보 영역 */}
      <div className="flex-1">
        <h2 className="text-lg font-semibold mb-1">{book.title}</h2>
        <p className="text-gray-600 mb-1">가격: {book.priceSales.toLocaleString()}원</p>
        <p className="text-gray-500">{book.description || '책 설명이 준비 중입니다.'}</p>
      </div>
    </div>
  );
};

export default SearchResultItem;
