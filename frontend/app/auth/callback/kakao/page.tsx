//auth/callback/kakao/page.tsx

'use client';

import { useRouter } from 'next/navigation';
import { useEffect } from 'react';

export default function KakaoCallback() {
  const router = useRouter();

  useEffect(() => {
    const handleLogin = async () => {
      try {
        // 백엔드에서 쿠키로 accessToken을 관리하므로 따로 토큰을 저장하지 않음
        const response = await fetch('http://localhost:8080/api/auth/me', {
          method: 'GET',
          credentials: 'include',
        });

        console.log('응답 상태:', response.status);

        if (!response.ok) throw new Error('Failed to fetch user data');

        const redirectUrl = sessionStorage.getItem('redirectUrl') || '/'; //기본값 홈페이지
        sessionStorage.removeItem('redirectUrl');

        router.push('redirectUrl'); // 프로필 페이지로 이동
      } catch (error) {
        console.error('Error fetching user data:', error);
        router.push('/'); // 로그인 실패 시 홈으로 이동
      }
    };

    handleLogin();
  }, [router]);

  return (
    <div className="flex min-h-screen items-center justify-center">
      <p>카카오 로그인 처리중...</p>
    </div>
  );
}