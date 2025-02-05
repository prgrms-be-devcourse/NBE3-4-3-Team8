'use client';
import React, { useState, KeyboardEvent } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '../hooks/useAuth';
import KakaoLoginButton from './KakaoLoginButton';

export default function NavBar() {
  const { user } = useAuth(); // useUser 훅 사용
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

  const handleLogout: () => Promise<void> = async () => {
    await fetch('http://localhost:8080/api/auth/logout', {
      method: 'POST',
      credentials: 'include',
    });
    window.location.href = '/'; // 새로고침으로 세션 초기화
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
            {user ? ( // 🔹 로그인한 경우
              <>
                <span className="cursor-pointer">{user.name}님</span> {/* 사용자 이름 표시 */}
                <button onClick={handleLogout} className="text-red-500">
                  로그아웃
                </button>{' '}
                {/* 로그아웃 버튼 */}
              </>
            ) : (
              <KakaoLoginButton /> // 🔹 로그인하지 않은 경우, 카카오 로그인 버튼 표시
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
