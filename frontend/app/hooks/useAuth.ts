//app/hooks/useAuth.ts
import { useState, useEffect } from 'react';

interface User {
  name: string;
  phoneNumber: string;
  memberType: 'USER' | 'ADMIN'; // Enum (사용자 역할)
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
      const token = localStorage.getItem('accessToken');
      if (!token) {
        setLoading(false);
        return;
      }
      try {
        const res = await fetch('http://localhost:8080/api/auth/me', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (res.ok) {
          const data = await res.json();
          setUser(data);
        } else {
          localStorage.removeItem('accessToken');
        }
      } catch (error) {
        console.error('로그인 정보 가져오기 실패:', error);
        localStorage.removeItem('accessToken');
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, []);

  const logout = () => {
    localStorage.removeItem('accessToken');
    setUser(null);
  };

  return { user, loading, logout };
}
