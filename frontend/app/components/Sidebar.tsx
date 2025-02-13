"use client";
import Link from "next/link";
import { useState, useEffect } from "react";
import Cookies from "js-cookie"; //  쿠키에서 JWT 토큰 가져오기
import { jwtDecode } from "jwt-decode"; //  올바른 방식


interface SidebarItemProps {
  to: string;
  label: string;
  subLabel?: string;
}

//  JWT 토큰의 페이로드 타입 정의
interface JwtPayload {
  name: string; // name 키 값
}

const Sidebar: React.FC = () => {
  const [username, setUsername] = useState<string | null>(null);

  useEffect(() => {
    try {
      const token = Cookies.get("token"); //  쿠키에서 JWT 토큰 가져오기
      if (token) {
        const decoded: JwtPayload = jwtDecode(token); //  JWT 디코딩
        console.log("username: ",decoded.name);
        setUsername(decoded.name); //  name 값 설정
      }
    } catch (error) {
      console.error("토큰을 해독할 수 없습니다:", error);
      setUsername("로그인 필요"); // 오류 발생 시 기본값 설정
    }
  }, []);

  return (
    <aside className="w-64 p-4 border-r min-h-screen bg-gray-100">
      <div className="flex flex-col items-center mb-6">
        <div className="w-20 h-20 bg-gray-300 rounded-full mb-2"></div>
        <p className="text-gray-700 font-semibold">{username || "유저 이름"}</p> {/*  JWT에서 가져온 유저 이름 표시 */}
      </div>
      <nav className="space-y-2">
        <SidebarItem to="/my/orders" label="쇼핑내역" subLabel="주문/배송목록" />
        <SidebarItem to="/library" label="라이브러리" subLabel="리뷰" />
        <SidebarItem to="/my/question" label="문의내역" subLabel="나의 문의내역" />
        <SidebarItem to="/my" label="회원정보" subLabel="회원정보 수정" />
      </nav>
    </aside>
  );
};

const SidebarItem: React.FC<SidebarItemProps> = ({ to, label, subLabel }) => {
  return (
    <Link href={to} className="block px-4 py-2 bg-white shadow rounded-lg hover:bg-gray-200">
      <p className="font-semibold text-gray-800">{label}</p>
      {subLabel && <p className="text-sm text-gray-600">{subLabel}</p>}
    </Link>
  );
};

export default Sidebar;