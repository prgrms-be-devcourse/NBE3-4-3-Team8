// utils/api.js

import axios from 'axios';

// 백엔드 API의 기본 URL 설정
const API_BASE_URL = 'http://localhost:8080'; // 백엔드 서버 주소

// Axios 인스턴스 생성
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
    withCredentials: true,
});

// 📌 전체 도서 목록 가져오기
export const fetchBooks = async (page = 0, pageSize = 10, sortType = 'PUBLISHED_DATE') => {
  try {
    const response = await api.get(`/books`, {
      params: { page, pageSize, sortType },
    });
    return response.data;
  } catch (error) {
    console.error('도서 목록을 가져오는 중 오류 발생:', error);
    throw error;
  }
};

// 📌 특정 도서 정보 가져오기
export const fetchBookById = async (bookId = 1) => {
  try {
    const response = await api.get(`/books/${bookId}`);
    return response.data;
  } catch (error) {
    console.error(`ID ${bookId}의 도서를 가져오는 중 오류 발생:`, error);
    throw error;
  }
};

// 📌 장바구니 추가
export const addToCart = async (cartItems) => {
  try {
    await api.post(`/cart`, { cartItems });
  } catch (error) {
    console.error('장바구니 추가 중 오류 발생:', error);
    throw error;
  }
};

// 📌 장바구니 조회
export const fetchCart = async () => {
  try {
    const response = await api.get(`/cart`);
    return response.data;
  } catch (error) {
    console.error('장바구니 조회 중 오류 발생:', error);
    throw error;
  }
};

// 📌 비로그인 사용자 장바구니 조회
export const fetchAnonymousCart = async (localCartData) => {
  try {
    const response = await api.post(`/cart/anonymous`, { cartItems: localCartData });
    return response.data;
  } catch (error) {
    console.error('익명 장바구니 조회 중 오류 발생:', error);
    throw error;
  }
};

// 장바구니 아이템(수량) 업데이트
export const updateCartItem = async (cartItems) => {
  try {
    await api.put(`/cart`, { cartItems });
  } catch (error) {
    console.error('장바구니 수량 변경 중 오류 발생:', error);
    throw error;
  }
};

// 장바구니 아이템 삭제
export const removeCartItems = async (cartItems) => {
  try {
    await api.delete(`/cart`, {
      data: { cartItems },
    });
  } catch (error) {
    console.error('장바구니 아이템 삭제 중 오류 발생:', error);
    throw error;
  }
};

// 📌 도서 이름 검색 (fetchSearchBooks)
export const fetchSearchBooks = async (
    page = 0,
    pageSize = 10,
    sortType = "PUBLISHED_DATE",
    searchType = "TITLE", // 기본값 TITLE
    keyword
) => {
    try {
        const response = await api.get(`/books/search`, {
            params: { page, pageSize, sortType, searchType, keyword },
        });
        return response.data;
    } catch (error) {
        console.error("도서 검색 중 오류 발생:", error);
        throw error;
    }
};

// 리뷰 등록
export const createReview = async (bookId, reviewData) => {
    try {
        const response = await api.post(`/reviews/${bookId}`, reviewData);
        return response.data;
    } catch (error) {
        console.error("리뷰 등록 중 오류 발생:", error);
        throw error;
    }
};

// 리뷰 수정 – 컨트롤러에서는 @RequestParam으로 content와 rating을 받으므로 쿼리 파라미터로 전송
export const updateReview = async (reviewId, reviewData) => {
    try {
        const response = await api.put(`/reviews/${reviewId}`, null, {
            params: reviewData,
        });
        return response.data;
    } catch (error) {
        console.error("리뷰 수정 중 오류 발생:", error);
        throw error;
    }
};

// 리뷰 삭제
export const deleteReview = async (reviewId) => {
    try {
        const response = await api.delete(`/reviews/${reviewId}`);
        return response.data;
    } catch (error) {
        console.error("리뷰 삭제 중 오류 발생:", error);
        throw error;
    }
};

// 본인 조회
export const fetchCurrentUser = async () => {
    try {
        const response = await api.get('/api/auth/me');
        return response.data; // MemberDto 형태의 데이터를 받음
    } catch (error) {
        console.error('현재 사용자 정보를 가져오는데 오류 발생:', error);
        throw error;
    }
};

export default api;