//app/hooks/useAuth.ts
import { useState, useEffect } from 'react';

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

export function useAuth() {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

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
      await fetch('http://localhost:8080/api/auth/logout', {
        method: 'POST',
        credentials: 'include', // ✅ 쿠키 포함 요청
      });
    } catch (error) {
      console.error('로그아웃 실패:', error);
    } finally {
      setUser(null);
    }
  };

  return { user, loading, logout };
}
