'use client';

import { useRouter } from 'next/navigation';
import { useAuth } from '@/app/hooks/useAuth';

export default function KakaoLoginButton() {
  const router = useRouter();
  const { isAuthenticated } = useAuth();

  const handleLogin = () => {
    // 이미 로그인된 상태라면 아무 동작도 하지 않음
    if (isAuthenticated) {
      console.log('이미 로그인되어 있습니다.');
      return;
    }

    const currentUrl = window.location.pathname + window.location.search; // 현재 페이지의 URL 저장
    sessionStorage.setItem('redirectUrl', currentUrl); // 세션 스토리지에 저장

    const loginUrl = `http://localhost:8080/oauth2/authorization/kakao?redirectUrl=${encodeURIComponent(currentUrl)}`;
    router.push(loginUrl);
  };

  return (
    <button
      onClick={handleLogin}
      className={`flex items-center gap-2 px-4 py-2 rounded font-medium border ${
        isAuthenticated
          ? 'bg-gray-100 text-gray-500 border-gray-300 cursor-not-allowed'
          : 'bg-white hover:bg-gray-200 text-black border-black'
      }`}
      disabled={isAuthenticated}
    >
      {isAuthenticated ? '로그인됨' : '카카오로 시작하기'}
    </button>
  );
}
