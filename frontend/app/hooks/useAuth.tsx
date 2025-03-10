'use client';

import React, { createContext, useState, useContext, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

type User = {
  id: string;
  email: string;
  name: string;
  profileImage?: string;
  // 필요한 다른 사용자 속성들
};

type AuthState = {
  user: User | null;
  loading: boolean;
  error: string | null;
  isAuthenticated: boolean;
};

type AuthContextType = AuthState & {
  login: (email: string, password: string) => Promise<boolean>;
  logout: () => Promise<boolean>;
  updateUserInfo: (updates: Partial<User>) => Promise<boolean>;
  refreshAuth: () => Promise<void>;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const refreshAccessToken = async (): Promise<boolean> => {
  try {
    await axios.post(
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
    return true;
  } catch (error) {
    console.error('토큰 갱신 실패:', error);
    return false;
  }
};

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [authState, setAuthState] = useState<AuthState>({
    user: null,
    loading: true,
    error: null,
    isAuthenticated: false,
  });
  const router = useRouter();

  const fetchUser = async () => {
    // 이미 인증 상태를 확인 중이거나 인증되지 않은 상태라면 요청을 보내지 않음
    if (!authState.loading && !authState.isAuthenticated) {
      setAuthState((prev) => ({ ...prev, loading: false }));
      return;
    }

    try {
      const response = await axios.get<User>(`${API_BASE_URL}/api/auth/me`, {
        withCredentials: true,
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        
      });

      console.log("userDate:")
      console.log(response.data)

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

  const login = async (email: string, password: string): Promise<boolean> => {
    try {
      const response = await axios.post(
        `${API_BASE_URL}/api/auth/login`,
        { email, password },
        {
          withCredentials: true,
          headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
          },
        },
      );

      if (response.status === 200) {
        await fetchUser();
        return true;
      }
      return false;
    } catch (error) {
      console.error('로그인 실패:', error);
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
    setAuthState((prev) => ({ ...prev, loading: true, isAuthenticated: true }));
    await fetchUser();
  };

  useEffect(() => {
    // 초기 로딩 시 한 번만 사용자 정보를 가져옴
    fetchUser();
  }, []);

  return (
    <AuthContext.Provider
      value={{
        ...authState,
        login,
        logout,
        updateUserInfo,
        refreshAuth,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

// Context를 활용하는 커스텀 훅
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};