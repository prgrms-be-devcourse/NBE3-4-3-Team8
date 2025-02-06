import axios from 'axios';

// 백엔드 API의 기본 URL 설정
const API_BASE_URL = "http://localhost:8080"; // 백엔드 서버 주소

// Axios 인스턴스 생성
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

// 📌 전체 도서 목록 가져오기
export const fetchBooks = async (page = 0, pageSize = 10, sortType = "PUBLISHED_DATE") => {
    try {
        const response = await api.get(`/books`, {
            params: { page, pageSize, sortType },
        });
        return response.data;
    } catch (error) {
        console.error("도서 목록을 가져오는 중 오류 발생:", error);
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
export const addToCart = async (bookId, memberId, quantity) => {
    try {
        await api.post(`/cart/${bookId}/${memberId}`, null, {
            params: { quantity },
        });
    } catch (error) {
        console.error("장바구니 추가 중 오류 발생:", error);
        throw error;
    }
};

// 📌 장바구니 조회
export const fetchCart = async (memberId) => {
    try {
        const response = await api.get(`/cart/${memberId}`);
        return response.data;
    } catch (error) {
        console.error("장바구니 조회 중 오류 발생:", error);
        throw error;
    }
};

// 장바구니 아이템(수량) 업데이트
export const updateCartItem = async (bookId, memberId, quantity) => {
    try {
        // PUT /cart/{book-id}/{member-id}?quantity=xxx
        await api.put(`/cart/${bookId}/${memberId}`, null, {
            params: { quantity },
        });
    } catch (error) {
        console.error('장바구니 수량 변경 중 오류 발생:', error);
        throw error;
    }
};

// 장바구니 아이템 삭제
export const removeCartItems = async (memberId, cartItems) => {
    // DELETE /cart/{member-id}, body: { cartItems: [ { bookId, quantity }, ... ] }
    try {
        await api.delete(`/cart/${memberId}`, {
            data: { cartItems },
        });
    } catch (error) {
        console.error('장바구니 아이템 삭제 중 오류 발생:', error);
        throw error;
    }
};

// 📌 도서 이름 검색 (fetchSearchBooks)
export const fetchSearchBooks = async (page = 0, pageSize = 10, sortType = "PUBLISHED_DATE", title) => {
    try {
        const response = await api.get(`/books/search`, {
            params: { page, pageSize, sortType, title },
        });
        return response.data;
    } catch (error) {
        console.error("도서 검색 중 오류 발생:", error);
        throw error;
    }
};

export default api;

