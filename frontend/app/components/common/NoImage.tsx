// components/common/NoImage.tsx
import React from 'react';

const NoImage = () => {
  return (
    <div className="absolute top-0 left-0 w-full h-full flex flex-col items-center justify-center bg-gray-100 rounded">
      <svg
        className="text-gray-400 mb-2"
        width="48"
        height="48"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        strokeWidth="1"
        strokeLinecap="round"
        strokeLinejoin="round"
      >
        <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z" />
        <circle cx="12" cy="10" r="3" />
        <path d="M4 20l4-5l4 5" />
        <path d="M12 20l4-5l4 5" />
        <line x1="4" y1="4" x2="20" y2="20" />
      </svg>
      <span className="text-gray-500 text-sm">이미지 없음</span>
    </div>
  );
};

export default NoImage;
