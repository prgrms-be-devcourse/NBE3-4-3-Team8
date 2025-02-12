'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { FaBox, FaChartBar, FaUsers } from 'react-icons/fa6';
import { useRouter } from 'next/navigation';

export default function DashboardLayout({ children }) {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [menuState, setMenuState] = useState<Record<string, boolean>>({});
  const router = useRouter(); // 페이지 이동을 위한 useRouter 훅

  const toggleMenu = (menu: string) => {
    setMenuState((prev) => ({ ...prev, [menu]: !prev[menu] }));
  };

  // 로그아웃 핸들러
  const handleLogout = async () => {
    await fetch('/api/auth/me/logout', { method: 'POST' });
    router.push('/admin/login');
  };

  return (
    <div className="flex h-screen w-full bg-white">
      {/* 헤더 */}
      <header className="fixed top-0 left-0 right-0 flex items-center justify-between px-6 py-4 bg-white shadow-md border-b border-gray-200 z-50">
        <div className="flex items-center space-x-4">
          {/* 햄버거 버튼 */}
          <button
            onClick={() => setIsSidebarOpen(!isSidebarOpen)}
            className="text-gray-800 focus:outline-none"
          >
            {isSidebarOpen ? (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={2}
                stroke="currentColor"
                className="w-6 h-6"
              >
                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
              </svg>
            ) : (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={2}
                stroke="currentColor"
                className="w-6 h-6"
              >
                <path strokeLinecap="round" strokeLinejoin="round" d="M4 6h16M4 12h16M4 18h16" />
              </svg>
            )}
          </button>
          <h1 className="text-xl font-extrabold text-gray-800">THE BOOK</h1>
        </div>
        <div className="flex items-center space-x-4 text-gray-800">
          <span>admin@thebook.co.kr</span>
          <button className="hover:text-blue-600" onClick={handleLogout}>
            로그아웃
          </button>
        </div>
      </header>

      {/* 메인 레이아웃 */}
      <div className="flex flex-grow pt-16">
        {/* 사이드바 */}
        {isSidebarOpen && (
          <aside className="fixed left-0 top-16 h-[calc(100vh-64px)] bg-white border-r border-gray-200 shadow-md z-40 w-60">
            <nav className="mt-6">
              <ul>
                {/* 주문 관리 */}
                <li>
                  <div
                    onClick={() => toggleMenu('sales')}
                    className="flex items-center px-6 py-4 font-semibold text-gray-800 cursor-pointer hover:bg-gray-100"
                  >
                    <FaChartBar className="w-6 h-6 mr-3" />
                    주문 관리
                  </div>
                  <ul className={menuState.sales ? 'block' : 'hidden'}>
                    <li className="py-2 text-gray-600 hover:text-blue-600 cursor-pointer pl-10">
                      <Link href="/admin/dashboard/orders">주문 내역</Link>
                    </li>
                  </ul>
                </li>

                {/* 상품 관리 */}
                <li>
                  <div
                    onClick={() => toggleMenu('product')}
                    className="flex items-center px-6 py-4 font-semibold text-gray-800 cursor-pointer hover:bg-gray-100"
                  >
                    <FaBox className="w-6 h-6 mr-3" />
                    상품 관리
                  </div>
                  <ul className={menuState.product ? 'block' : 'hidden'}>
                    <li className="py-2 text-gray-600 hover:text-blue-600 cursor-pointer pl-10">
                      <Link href="/admin/dashboard/books/register">상품 등록</Link>
                    </li>
                    <li className="py-2 text-gray-600 hover:text-blue-600 cursor-pointer pl-10">
                      <Link href="/admin/dashboard/books/list">상품 조회</Link>
                    </li>
                  </ul>
                </li>

                {/* 고객 관리 */}
                <li>
                  <div
                    onClick={() => toggleMenu('customer')}
                    className="flex items-center px-6 py-4 font-semibold text-gray-800 cursor-pointer hover:bg-gray-100"
                  >
                    <FaUsers className="w-6 h-6 mr-3" />
                    고객 관리
                  </div>
                  <ul className={menuState.customer ? 'block' : 'hidden'}>
                    <li className="py-2 text-gray-600 hover:text-blue-600 cursor-pointer pl-10">
                      <Link href="/admin/dashboard/qna">문의 내역</Link>
                    </li>
                  </ul>
                </li>
              </ul>
            </nav>
          </aside>
        )}

        {/* 메인 콘텐츠 */}
        <main
          className={`flex-grow p-6 overflow-auto transition-all duration-300 ease-in-out ${
            isSidebarOpen ? 'ml-60' : 'ml-0'
          }`}
        >
          {children}
        </main>
      </div>
    </div>
  );
}
