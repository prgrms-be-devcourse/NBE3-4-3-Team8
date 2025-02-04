// utils/cart.ts
import { getMemberId, isLoggedIn } from '@/utils/auth';

const CART_KEY = 'guest_cart';

export const getGuestCart = (): CartItem[] => {
  if (typeof window === 'undefined') return [];
  const cart = localStorage.getItem(CART_KEY);
  return cart ? JSON.parse(cart) : [];
};

export const saveGuestCart = (cart: CartItem[]) => {
  localStorage.setItem(CART_KEY, JSON.stringify(cart));
};

interface CartItem {
  bookId: number;
  quantity: number;
}

// API 통신 함수
export const addToCart = async (bookId: number, memberId: number, quantity: number) => {
  if (isLoggedIn()) {
    const memberId = getMemberId();
    if (!memberId) throw new Error('로그인이 필요합니다');

    const response = await fetch(`http://localhost:8080/cart/${bookId}/${memberId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ bookId, quantity }),
    });

    if (!response.ok) throw new Error('장바구니 추가 실패');
  } else {
    const cart = getGuestCart();
    const existingItem = cart.find((item) => item.bookId === bookId);

    if (existingItem) {
      existingItem.quantity += quantity;
    } else {
      cart.push({ bookId, quantity });
    }

    saveGuestCart(cart);
  }
};

// 로그인 시 호출할 동기화 함수
export const syncGuestCart = async () => {
  if (!isLoggedIn()) return;

  const guestCart = getGuestCart();
  if (guestCart.length === 0) return;

  const memberId = getMemberId();
  if (!memberId) throw new Error('회원 ID를 찾을 수 없습니다.');

  try {
    await Promise.all(
      guestCart.map((item) =>
        fetch(`http://localhost:8080/cart/${item.bookId}/${memberId}`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            bookId: item.bookId,
            quantity: item.quantity,
          }),
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
    const memberId = getMemberId();
    if (!memberId) throw new Error('회원 ID를 찾을 수 없습니다.');

    const response = await fetch(`http://localhost:8080/cart/${memberId}`);
    if (!response.ok) throw new Error('장바구니 조회 실패');
    return response.json();
  } else {
    return getGuestCart();
  }
};
