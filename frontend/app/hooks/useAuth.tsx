'use client';

import { useState, useEffect, createContext, useContext } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';

interface DeliveryInformation {
  id: number;
  addressName: string;
  address: string;
  detailAddress: string;
  phoneNumber: string;
  isDefaultAddress: boolean;
}

interface User {
  id: number;
  name: string;
  email: string;
  phoneNumber: string;
  memberType: 'USER' | 'ADMIN';
  oauthId: string;
  deliveryInformations: DeliveryInformation[];
  profileImageUrl: string;
}

interface AuthState {
  user: User | null;
  loading: boolean;
  error: string | null;
  isAuthenticated: boolean;
}

interface AuthContextProps extends AuthState {
  logout: () => Promise<boolean>;
  refreshAuth: () => Promise<void>;
  updateUserInfo: (updates: Partial<User>) => Promise<boolean>;
}

const API_BASE_URL = 'http://localhost:8080';

const AuthContext = createContext<AuthContextProps>({
  user: null,
  loading: true,
  error: null,
  isAuthenticated: false,
  logout: async () => false,
  refreshAuth: async () => {},
  updateUserInfo: async () => false,
});

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [authState, setAuthState] = useState<AuthState>({
    user: null,
    loading: true,
    error: null,
    isAuthenticated: false,
  });
  const router = useRouter();

  const fetchUser = async () => {
    try {
      const response = await axios.get<User>(`${API_BASE_URL}/api/auth/me`, {
        withCredentials: true,
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
      });

      setAuthState({
        user: response.data,
        loading: false,
        error: null,
        isAuthenticated: true,
      });
    } catch (error: any) {
      if (error.response?.status === 401) {
        const refreshed = await refreshAccessToken();
        if (refreshed) {
          return fetchUser();
        }

        setAuthState({
          user: null,
          loading: false,
          error: '로그인이 필요합니다',
          isAuthenticated: false,
        });
        return;
      }

      setAuthState({
        user: null,
        loading: false,
        error: '인증에 실패했습니다',
        isAuthenticated: false,
      });
    }
  };

  const refreshAccessToken = async (): Promise<boolean> => {
    try {
      const response = await axios.post(
        `${API_BASE_URL}/api/auth/refresh`,
        {},
        {
          withCredentials: true,
          headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
          },
        },
      );

      return response.status === 200;
    } catch (error) {
      console.error('토큰 갱신 실패:', error);
      return false;
    }
  };

  const logout = async (): Promise<boolean> => {
    try {
      await axios.post(
        `${API_BASE_URL}/api/auth/me/logout`,
        {},
        {
          withCredentials: true,
          headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
          },
        },
      );

      setAuthState({
        user: null,
        loading: false,
        error: null,
        isAuthenticated: false,
      });

      router.replace('/');
      return true;
    } catch (error) {
      console.error('로그아웃 실패:', error);
      return false;
    }
  };

  const updateUserInfo = async (updates: Partial<User>): Promise<boolean> => {
    try {
      const response = await axios.put(`${API_BASE_URL}/api/auth/me/my`, updates, {
        withCredentials: true,
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
      });

      if (response.status === 200) {
        setAuthState((prev) => ({
          ...prev,
          user: response.data,
        }));
        return true;
      }
      return false;
    } catch (error) {
      console.error('사용자 정보 업데이트 실패:', error);
      return false;
    }
  };

  const refreshAuth = async () => {
    setAuthState((prev) => ({ ...prev, loading: true }));
    await fetchUser();
  };

  useEffect(() => {
    fetchUser();
  }, []);

  return (
    <AuthContext.Provider
      value={{
        ...authState,
        logout,
        refreshAuth,
        updateUserInfo,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

// Context를 활용하는 커스텀 훅
export const useAuth = () => useContext(AuthContext);
