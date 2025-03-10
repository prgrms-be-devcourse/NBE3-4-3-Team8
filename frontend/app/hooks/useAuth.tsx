'use client';
import React, { createContext, useState, useContext, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

type User = {
    id: string;
    email: string;
    name: string;
    profileImageUrl?: string;
    memberType: string;
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
            const response = await axios.get(`${API_BASE_URL}/api/auth/me`, {
                withCredentials: true,
            });

            const userData = response.data.attributes; // attributes 내부 데이터만 추출

            setAuthState({
                user: {
                    id: userData.id,
                    email: userData.email,
                    name: userData.name || '사용자', // 기본값 설정
                    profileImageUrl: userData.profileImageUrl || '',
                    memberType: userData.memberType,
                },
                loading: false,
                error: null,
                isAuthenticated: true,
            });
        } catch (error) {
            console.error('사용자 정보 가져오기 실패:', error);
            setAuthState({
                user: null,
                loading: false,
                error: '사용자 정보를 가져오지 못했습니다.',
                isAuthenticated: false,
            });
        }
    };

    const login = async (email: string, password: string): Promise<boolean> => {
        try {
            const response = await axios.post(
                `${API_BASE_URL}/api/auth/login`,
                { email, password },
                { withCredentials: true }
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
            await axios.post(`${API_BASE_URL}/api/auth/me/logout`, {}, { withCredentials: true });

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
            });

            if (response.status === 200) {
                setAuthState((prev) => ({
                    ...prev,
                    user: { ...prev.user!, ...updates },
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

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error('useAuth must be used within an AuthProvider');
    return context;
};