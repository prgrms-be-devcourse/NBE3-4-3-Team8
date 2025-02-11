'use client';

import { useState, useEffect, createContext, useContext } from 'react';
import { useRouter } from 'next/navigation';

interface User {
  name: string;
  phoneNumber: string;
  memberType: 'USER' | 'ADMIN';
  oauthId: string;
  email: string;
  deliveryInformations: DeliveryInformation[];
}

interface DeliveryInformation {
  id: number;
  address: string;
  isDefaultAddress: boolean;
}

interface AuthContextProps {
  user: User | null;
  loading: boolean;
  logout: () => Promise<void>;
}

// Context 생성
const AuthContext = createContext<AuthContextProps>({
  user: null,
  loading: true,
  logout: async () => {},
});

// Provider 컴포넌트
export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const fetchUser = async () => {
      try {
        // ✅ 쿠키 포함 요청 (credentials: 'include' 추가)
        const res = await fetch('http://localhost:8080/api/auth/me', {
          method: 'GET',
          credentials: 'include', // ✅ 쿠키를 자동으로 포함하도록 설정
        });

        if (res.ok) {
          const data = await res.json();
          setUser(data);
        } else {
          setUser(null);
        }
      } catch (error) {
        console.error('로그인 정보 가져오기 실패:', error);
        setUser(null);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, []);

  const logout = async () => {
    try {
      // ✅ 백엔드 로그아웃 API 호출
      await fetch('http://localhost:8080/api/auth/me/logout', {
        method: 'POST',
        credentials: 'include', // ✅ 쿠키 포함 요청
      });
      setUser(null);
      router.replace('/'); // 로그아웃 후 홈으로 이동
    } catch (error) {
      console.error('로그아웃 실패:', error);
    }
  };

  return <AuthContext.Provider value={{ user, loading, logout }}>{children}</AuthContext.Provider>;
};

// Context를 활용하는 커스텀 훅
export const useAuth = () => useContext(AuthContext);
