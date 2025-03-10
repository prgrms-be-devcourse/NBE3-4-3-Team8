// 주문 유형 enum
export enum OrderType {
  CART = 'CART', // 장바구니 결제
  DIRECT = 'DIRECT', // 바로 결제
}

// 주문 상품 데이터
export interface OrderItemData {
  bookId: number;
  title: string;
  coverImage: string;
  price: number;
  quantity: number;
}

// 결제 정보
export interface PaymentInfo {
  cartList: OrderItemData[];
  priceStandard: number;
  pricesSales: number;
  orderId: string;
}

// 주문 요청 데이터
export interface OrderRequestData {
  postCode: string;
  fullAddress: string;
  recipient: string;
  phone: string;
  paymentMethod: string;
  bookId?: number;
  quantity?: number;
  orderType: OrderType;
}
