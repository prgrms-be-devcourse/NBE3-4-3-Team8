import api from './api';

const LOCAL_STORAGE_CART_KEY = 'localCart';

interface CartItem {
  bookId: number;
  quantity: number;
  isAddToCart: boolean;
}

const getLocalCart = (): CartItem[] => {
  const localCart = localStorage.getItem(LOCAL_STORAGE_CART_KEY);
  return localCart ? JSON.parse(localCart) : [];
};

const saveLocalCart = (cart: CartItem[]) => {
  localStorage.setItem(LOCAL_STORAGE_CART_KEY, JSON.stringify(cart));
};

export const addToCart = async (cartItems: CartItem[]) => {
  try {
    await api.post('/cart', { cartItems });
  } catch (error: any) {
    if (error.response && error.response.status === 401) {
      // 인증되지 않은 사용자의 경우 로컬 스토리지에 저장
      const localCart = getLocalCart();
      cartItems.forEach((item) => {
        const existingItemIndex = localCart.findIndex(
          (localItem) => localItem.bookId === item.bookId,
        );
        if (existingItemIndex !== -1) {
          localCart[existingItemIndex].quantity += item.quantity;
        } else {
          localCart.push(item);
        }
      });
      saveLocalCart(localCart);
    } else {
      throw error;
    }
  }
};

export const fetchCart = async () => {
  try {
    const response = await api.get('/cart');
    return response.data;
  } catch (error: any) {
    if (error.response && error.response.status === 401) {
      // 인증되지 않은 사용자의 경우 로컬 스토리지에서 데이터 반환
      return getLocalCart();
    }
    throw error;
  }
};

export const updateCartItem = async (cartItems: CartItem[]) => {
  try {
    await api.put('/cart', { cartItems });
  } catch (error: any) {
    if (error.response && error.response.status === 401) {
      // 인증되지 않은 사용자의 경우 로컬 스토리지 업데이트
      saveLocalCart(cartItems);
    } else {
      throw error;
    }
  }
};

export const removeCartItems = async (cartItems: CartItem[]) => {
  try {
    await api.delete('/cart', { data: { cartItems } });
  } catch (error: any) {
    if (error.response && error.response.status === 401) {
      // 인증되지 않은 사용자의 경우 로컬 스토리지에서 제거
      const localCart = getLocalCart();
      const updatedCart = localCart.filter(
        (item) => !cartItems.some((cartItem) => cartItem.bookId === item.bookId),
      );
      saveLocalCart(updatedCart);
    } else {
      throw error;
    }
  }
};