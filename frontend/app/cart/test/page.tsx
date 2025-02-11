'use client';

import { useAuth } from '@/app/hooks/useAuth';

export default function MyPage() {
  const { user, loading, logout } = useAuth();

  if (loading) return <p>로딩 중...</p>;

  return (
    <div>
      {user ? (
        <>
          <p>안녕하세요, {user.name}님!</p>
          <button onClick={logout}>로그아웃</button>
        </>
      ) : (
        <p>로그인이 필요합니다.</p>
      )}
    </div>
  );
}
