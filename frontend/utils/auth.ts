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

export const getMemberId = () => {
  const cookies = document.cookie.split(';').reduce(
    (acc, cookie) => {
      const [name, value] = cookie.trim().split('=');
      acc[name] = value;
      return acc;
    },
    {} as Record<string, string>,
  );
  return cookies['member_id'] || null;
};
