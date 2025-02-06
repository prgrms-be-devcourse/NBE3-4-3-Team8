"use client";
import React, { useState, KeyboardEvent } from 'react';
import { useRouter } from 'next/navigation';

export default function NavBar() {
    const router = useRouter();
    const [searchText, setSearchText] = useState('');

    const handleSearch = () => {
        // 검색 버튼 클릭 시 /search?title=검색어 로 이동
        router.push(`/search?title=${encodeURIComponent(searchText)}`);
    };

    const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            // 엔터 시 /search?title=검색어 로 이동
            router.push(`/search?title=${encodeURIComponent(searchText)}`);
        }
    };

    return (
        <header className="border-b border-black">
            <div className="max-w-7xl mx-auto px-4">
                <div className="flex items-center justify-between h-16">
                    {/* THE BOOK 클릭 시 메인 페이지로 이동 */}
                    <div
                        className="text-xl font-bold text-black cursor-pointer"
                        onClick={() => router.push('/')}
                    >
                        THE BOOK
                    </div>

                    <div className="flex-1 max-w-2xl mx-8">
                        <div className="relative">
                            <input
                                type="text"
                                placeholder="SEARCH"
                                value={searchText}
                                onChange={(e) => setSearchText(e.target.value)}
                                onKeyDown={handleKeyDown}
                                className="w-full px-4 py-2 border border-black rounded-full focus:outline-none focus:ring-2 focus:ring-black"
                            />
                            <button
                                onClick={handleSearch}
                                className="absolute right-3 top-1/2 -translate-y-1/2 text-black"
                            >
                                🔍
                            </button>
                        </div>
                    </div>

                    <nav className="flex gap-6 text-sm text-black">
                        <span className="cursor-pointer">로그인</span>
                        <span className="cursor-pointer">장바구니</span>
                        <span className="cursor-pointer">고객센터</span>
                    </nav>
                </div>
            </div>
        </header>
    );
}
