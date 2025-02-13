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