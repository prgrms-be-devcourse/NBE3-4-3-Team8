'use client';

import { useRouter } from 'next/navigation';

export default function KakaoLoginButton() {
  const router = useRouter();

  const handleLogin = () => {
    const currentUrl = window.location.pathname + window.location.search; // 현재 페이지의 URL 저장
    sessionStorage.setItem('redirectUrl', currentUrl); // ✅ 세션 스토리지에 저장

    const loginUrl = `http://localhost:8080/oauth2/authorization/kakao?redirectUrl=${encodeURIComponent(currentUrl)}`;
    router.push(loginUrl);
  };

  return (
    <button
      onClick={handleLogin}
      className="flex items-center gap-2 bg-white hover:bg-gray-200 px-4 py-2 rounded text-black font-medium border border-black"
    >
      카카오로 시작하기
    </button>
  );
}