//auth/callback/kakao/page.tsx
'use client';
import { useRouter, useSearchParams } from 'next/navigation';
import { useEffect } from 'react';

export default function KakaoCallback() {
  const router = useRouter();
  const searchParams = useSearchParams();

  useEffect(() => {
    const handleLogin = async () => {
      try {
        const token = searchParams.get('token');

        if (!token) throw new Error('No token found in query string');

        localStorage.setItem('accessToken', token);

        const response = await fetch('http://localhost:8080/api/auth/me', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) throw new Error('Failed to fetch user data');

        router.push('/my/profile'); // 프로필 페이지로 이동
      } catch (error) {
        console.error('Error fetching user data:', error);
        router.push('/'); // 로그인 실패 시 홈으로 이동
      }
    };

    handleLogin();
  }, [router, searchParams]);

  return (
    <div className="flex min-h-screen items-center justify-center">
      <p>카카오 로그인 처리중...</p>
    </div>
  );
}
