'use client';
import { useEffect, useState } from 'react';
import Image from 'next/image';
import Link from 'next/link';
import { useAuth } from '@/app/hooks/useAuth';

const Sidebar = () => {
  const { user, logout } = useAuth();


  
  return (
    <aside className="w-64 p-4 bg-gray-100 min-h-screen">
      <div className="flex flex-col items-center mb-6">
        <Image
          src={user?.profileImageUrl || '/images/default-profile.png'}
          alt="User Profile"
          width={80}
          height={80}
          className="rounded-full border border-gray-300"
        />
        <p className="mt-2 font-semibold">{user?.name || '게스트'}</p>
      </div>
      <nav>
        <ul className="space-y-4">
          <li>
            <Link href="/my/orders" className="block p-2 bg-white rounded shadow">
              쇼핑내역
            </Link>
          </li>
          <li>
            <Link href="/my/reviews" className="block p-2 bg-white rounded shadow">
              리뷰내역
            </Link>
          </li>
          <li>
            <Link href="/my/question" className="block p-2 bg-white rounded shadow">
              문의내역
            </Link>
          </li>
          <li>
            <Link href="/my" className="block p-2 bg-white rounded shadow">
              회원정보
            </Link>
          </li>
        </ul>
      </nav>
    </aside>
  );
};

export default Sidebar;