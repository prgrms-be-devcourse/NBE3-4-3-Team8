<<<<<<< HEAD
'use client';
import React, { useState, KeyboardEvent } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '../hooks/useAuth';
import KakaoLoginButton from './KakaoLoginButton';
=======
"use client";
import React, { useState, KeyboardEvent } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "../hooks/useAuth";
import KakaoLoginButton from "./KakaoLoginButton";
import { SearchType } from "@/types/book";

const searchOptions = [
    { label: "제목", value: SearchType.TITLE },
    { label: "저자", value: SearchType.AUTHOR },
    { label: "ISBN13", value: SearchType.ISBN13 },
    { label: "출판사", value: SearchType.PUBLISHER },
];
>>>>>>> origin/dev

export default function NavBar() {
    const { user, logout } = useAuth();
    const router = useRouter();
    const [searchText, setSearchText] = useState("");
    const [selectedSearchType, setSelectedSearchType] = useState<SearchType>(SearchType.TITLE);

    const handleSearch = () => {
        if (!searchText.trim()) return;
        router.push(
            `/search?keyword=${encodeURIComponent(searchText)}&searchType=${selectedSearchType}`
        );
        setSearchText("");
    };

  const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const handleLogout = async () => {
    await logout(); // ✅ 로그아웃 요청
    router.push('/'); // ✅ 로그아웃 후 홈으로 이동
  };

<<<<<<< HEAD
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
          <div className="flex items-center">
            <input
              type="text"
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              onKeyDown={handleKeyDown}
              className="border border-gray-300 rounded px-2 py-1"
            />
            <button
              onClick={handleSearch}
              className="ml-2 bg-blue-500 text-white px-4 py-1 rounded"
            >
              🔍
            </button>
          </div>
         <nav className="flex gap-6 text-sm text-black">
           {user ? ( // ✅ 로그인한 경우
             <>
               <span className="cursor-pointer">{user.name}님</span> {/* 사용자 이름 표시 */}
               <button onClick={handleLogout} className="text-red-500">
                 로그아웃
               </button>{' '}
               {/* ✅ 로그아웃 버튼 */}
               <span
                 className="cursor-pointer"
                 onClick={() => router.push('/my/orders')} // 마이페이지 -> 주문내역 페이지로 이동
               >
                 마이페이지
               </span>
             </>
           ) : (
             <KakaoLoginButton /> // ✅ 로그인하지 않은 경우, 카카오 로그인 버튼 표시
           )}
           <span className="cursor-pointer" onClick={() => router.push('/cart')}>
             장바구니
           </span>
           <span className="cursor-pointer" onClick={() => router.push('/support')}>
             고객센터
           </span>
         </nav>
        </div>
      </div>
    </header>
  );
}
=======
    return (
        <header className="bg-white shadow border-b border-black">
            <div className="max-w-7xl mx-auto px-4">
                <div className="flex items-center justify-between h-16">
                    {/* 로고 영역 */}
                    <div
                        className="text-2xl font-bold text-gray-800 cursor-pointer"
                        onClick={() => router.push("/")}
                    >
                        THE BOOK
                    </div>

                    {/* 검색 영역 */}
                    <div className="flex-1 max-w-2xl mx-10 flex items-center">
                        <div className="flex w-full max-w-2xl">
                            <select
                                value={selectedSearchType}
                                onChange={(e) => setSelectedSearchType(e.target.value as SearchType)}
                                className="px-3 py-2 border border-gray-300 bg-gray-50 text-gray-700 rounded-l-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                            >
                                {searchOptions.map((option) => (
                                    <option key={option.value} value={option.value}>
                                        {option.label}
                                    </option>
                                ))}
                            </select>
                            <input
                                type="text"
                                placeholder="검색어를 입력하세요"
                                value={searchText}
                                onChange={(e) => setSearchText(e.target.value)}
                                onKeyDown={handleKeyDown}
                                className="w-full px-3 py-2 border-t border-b border-gray-300 bg-gray-50 text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                            />
                            <button
                                onClick={handleSearch}
                                className="px-4 py-2 border border-gray-300 bg-white text-blue-500 rounded-r-md hover:bg-gray-100 transition-colors flex items-center justify-center"
                            >
                                🔍
                            </button>
                        </div>
                    </div>

                    {/* 우측 네비게이션 */}
                    <nav className="flex gap-6 text-sm text-gray-700">
                        {user ? (
                            <>
                                <span className="cursor-pointer">{user.name}님</span>
                                <button onClick={handleLogout} className="text-red-500">
                                    로그아웃
                                </button>
                            </>
                        ) : (
                            <KakaoLoginButton />
                        )}
                        <span
                            className="cursor-pointer hover:text-blue-500 transition-colors"
                            onClick={() => router.push("/cart")}
                        >
                            장바구니
                        </span>
                        <span
                            className="cursor-pointer hover:text-blue-500 transition-colors"
                            onClick={() => router.push("/support")}
                        >
                            고객센터
                        </span>
                    </nav>
                </div>
            </div>
        </header>
    );
}
>>>>>>> origin/dev
