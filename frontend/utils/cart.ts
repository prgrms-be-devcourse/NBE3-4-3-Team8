// utils/cart.ts

const CART_KEY = 'guest_cart';
const API_URL = 'http://localhost:8080/cart/';
const JWT_COOKIE_NAME = 'jwtToken';

interface CartItem {
  bookId: number;
  quantity: number;
}

// JWT 토큰 쿠키 존재 여부로 로그인 상태 확인
const isLoggedIn = () => {
  return document.cookie
    .split(';')
    .map((item) => item.trim())
    .some((item) => item.startsWith(`${JWT_COOKIE_NAME}=`));
};

export const getGuestCart = (): CartItem[] => {
  if (typeof window === 'undefined') return [];
  const cart = localStorage.getItem(CART_KEY);
  return cart ? JSON.parse(cart) : [];
};

export const saveGuestCart = (cart: CartItem[]) => {
  localStorage.setItem(CART_KEY, JSON.stringify(cart));
};

export const addToCart = async (bookId: number, quantity: number) => {
  try {
    console.log('JWT 쿠키 존재:', isLoggedIn());
    if (isLoggedIn()) {
      // 로그인 사용자: API 서버 연동
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ bookId, quantity }),
        credentials: 'include', // 쿠키 포함 설정
      });

      if (!response.ok) throw new Error('장바구니 추가 실패');
    } else {
      // 비로그인 사용자: 로컬 스토리지 사용
      const cart = getGuestCart();
      const existingItem = cart.find((item) => item.bookId === bookId);

      if (existingItem) {
        existingItem.quantity += quantity;
      } else {
        cart.push({ bookId, quantity });
      }
      saveGuestCart(cart);
    }
  } catch (error) {
    console.error('장바구니 처리 중 오류:', error);
    throw error;
  }
};

export const syncGuestCart = async () => {
  if (!isLoggedIn()) return;

  const guestCart = getGuestCart();
  if (guestCart.length === 0) return;

  try {
    await Promise.all(
      guestCart.map((item) =>
        fetch(API_URL, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            bookId: item.bookId,
            quantity: item.quantity,
          }),
          credentials: 'include', // 쿠키 포함 설정
        }),
      ),
    );
    localStorage.removeItem(CART_KEY);
  } catch (error) {
    console.error('장바구니 동기화 실패', error);
    throw error;
  }
};

export const fetchCart = async (): Promise<CartItem[]> => {
  if (isLoggedIn()) {
    const response = await fetch(API_URL, {
      credentials: 'include', // 쿠키 포함 설정
    });

    if (!response.ok) throw new Error('장바구니 조회 실패');
    return response.json();
  } else {
    return getGuestCart();
  }
};
