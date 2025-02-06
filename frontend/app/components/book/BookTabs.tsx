// components/book/BookTabs.tsx
'use client';
import React, { useState } from 'react';
import { ReviewList } from './ReviewList';
import { ExchangeReturnInfo } from './ExchangeReturnInfo';

interface BookTabsProps {
  bookId: number;
}

export const BookTabs: React.FC<BookTabsProps> = ({ bookId }) => {
  const [activeTab, setActiveTab] = useState('info');

  return (
    <div className="mt-12">
      <div className="flex border-b border-black">
        <button
          className={`px-8 py-3 ${
            activeTab === 'info'
              ? 'border-t border-x border-black bg-white -mb-px'
              : 'text-gray-500'
          }`}
          onClick={() => setActiveTab('info')}
        >
          상품 정보
        </button>
        <button
          className={`px-8 py-3 ${
            activeTab === 'reviews'
              ? 'border-t border-x border-black bg-white -mb-px'
              : 'text-gray-500'
          }`}
          onClick={() => setActiveTab('reviews')}
        >
          리뷰
        </button>
        <button
          className={`px-8 py-3 ${
            activeTab === 'exchange'
              ? 'border-t border-x border-black bg-white -mb-px'
              : 'text-gray-500'
          }`}
          onClick={() => setActiveTab('exchange')}
        >
          교환/반품
        </button>
      </div>

      {activeTab === 'reviews' && <ReviewList bookId={bookId} />}
      {activeTab === 'exchange' && <ExchangeReturnInfo />}
    </div>
  );
};
