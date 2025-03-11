'use client';

import { useRouter } from 'next/navigation';
import { useEffect } from 'react';
import { useAuth } from '@/app/hooks/useAuth';

export default function KakaoCallback() {
  const router = useRouter();
  const { refreshAuth } = useAuth();

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

        // 인증 상태 갱신
        await refreshAuth();

        // 리다이렉트 URL 처리
        const redirectUrl = sessionStorage.getItem('redirectUrl') || '/';
        sessionStorage.removeItem('redirectUrl');

        router.push(redirectUrl);
      } catch (error) {
        console.error('Error fetching user data:', error);
        router.push('/'); // 로그인 실패 시 홈으로 이동
      }
    };

    handleLogin();
  }, [router, refreshAuth]);

  return (
    <div className="flex min-h-screen items-center justify-center">
      <p>카카오 로그인 처리중...</p>
    </div>
  );
}
