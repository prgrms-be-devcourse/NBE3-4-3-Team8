import React from 'react';

export const BookTabs: React.FC = () => {
    return (
        <div className="mt-12">
            <div className="flex border-b border-black">
                <button className="px-8 py-3 border-t border-x border-black bg-white -mb-px">
                    상품 정보
                </button>
                <button className="px-8 py-3 text-gray-500">리뷰(0)</button>
                <button className="px-8 py-3 text-gray-500">교환/반품</button>
            </div>
            {/* 탭 컨텐츠 ... */}
        </div>
    );
};