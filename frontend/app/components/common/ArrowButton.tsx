// components/common/ArrowButton.tsx
import React from 'react';

interface ButtonProps {
  direction: 'left' | 'right';
  onClick: () => void;
}

const ArrowButton: React.FC<ButtonProps> = ({ direction, onClick }) => {
  return (
    <button
      onClick={onClick}
      className={`absolute top-1/2 -translate-y-1/2 z-10 bg-white/80 hover:bg-white shadow-md rounded-full p-2 ${direction === 'left' ? 'left-0' : 'right-0'}`}
      aria-label={direction === 'left' ? '이전' : '다음'}
    >
      <svg className="w-6 h-6 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeWidth={2}
          d={direction === 'left' ? 'M15 19l-7-7 7-7' : 'M9 5l7 7-7 7'}
        />
      </svg>
    </button>
  );
};

export default ArrowButton;