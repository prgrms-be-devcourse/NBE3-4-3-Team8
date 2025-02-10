'use client';

import Cookies from 'js-cookie';

export const saveTokenToCookie = (token: string) => {
  if (!token) return;

  Cookies.set('jwtToken', token, {
    expires: 7, // 7일 후 만료
    path: '/',
    secure: process.env.NODE_ENV === 'production', // 배포 환경에서만 secure 설정
    sameSite: 'Strict',
  });
};

export const getTokenFromCookie = (): string | undefined => {
  return Cookies.get('jwtToken');
};
// utils/auth.ts
export const isLoggedIn = () => {
  if (typeof window === 'undefined') return false;
  const cookies = document.cookie.split(';').reduce(
    (acc, cookie) => {
      const [name, value] = cookie.trim().split('=');
      acc[name] = value;
      return acc;
    },
    {} as Record<string, string>,
  );
  return !!cookies['user_session'];
};

export const getJwtToken = () => {
  if (typeof window === 'undefined') return null;

  const cookies = document.cookie.split(';').reduce(
    (acc, cookie) => {
      const [name, value] = cookie.trim().split('=');
      acc[name] = value;
      return acc;
    },
    {} as Record<string, string>,
  );

  return cookies['jwtToken'] || null;
};
