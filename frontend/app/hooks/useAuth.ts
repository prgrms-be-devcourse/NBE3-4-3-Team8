"use client";

import {useEffect, useState} from 'react';
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
}

interface AuthState {
    user: User | null;
    loading: boolean;
    error: string | null;
    isAuthenticated: boolean;
}

const API_BASE_URL = 'http://localhost:8080';

export function useAuth() {
    const [authState, setAuthState] = useState<AuthState>({
        user: null,
        loading: true,
        error: null,
        isAuthenticated: false,
    });

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
                // 토큰이 만료되었을 수 있으므로 토큰 갱신 시도
                const refreshed = await refreshAccessToken();
                if (refreshed) {
                    // 토큰 갱신 성공시 사용자 정보 다시 요청
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

    const refreshAuth = () => {
        setAuthState((prev) => ({...prev, loading: true}));
        return fetchUser();
    };

    useEffect(() => {
        fetchUser();
    }, []);

    return {
        ...authState,
        refreshAuth,
        logout,
        updateUserInfo,
    };
}
