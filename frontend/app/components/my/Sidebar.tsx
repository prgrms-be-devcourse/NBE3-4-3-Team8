'use client';
import { useEffect, useState } from 'react';
import Image from 'next/image';
import Link from 'next/link';
import { MemberDto } from "./types"; // ✅ MemberDto 타입 불러오기

const Sidebar = () => {
  // ✅ 사용자 정보 상태
  const [user, setUser] = useState<MemberDto | null>(null);

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/auth/me", {
          method: "GET",
          credentials: "include", // JWT 쿠키 포함
        });

        if (!response.ok) {
          throw new Error("사용자 정보를 불러올 수 없습니다.");
        }

        const data: MemberDto = await response.json();

        // ✅ 사용자 정보 설정
        setUser(data);
      } catch (error) {
        console.error("사용자 정보를 불러오는 중 오류 발생:", error);
      }
    };

    fetchUserInfo();
  }, []);

  return (
    <aside className="w-64 p-4 bg-gray-100 min-h-screen">
      <div className="flex flex-col items-center mb-6">
        <Image
          src={ "/default-profile.png"} // ✅ 프로필 이미지 표시
          alt="User Profile"
          width={80}
          height={80}
          className="rounded-full border border-gray-300"
        />
        <p className="mt-2 font-semibold">
          {user?.name || "회원이름"} {/* ✅ 사용자 이름 표시 */}
        </p>
      </div>
      <nav>
        <ul className="space-y-4">
          <li>
            <Link href="/my/orders" className="block p-2 bg-white rounded shadow">
              쇼핑내역
            </Link>
          </li>
          <li>
            <Link href="/mypage/library" className="block p-2 bg-white rounded shadow">
              라이브러리
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
