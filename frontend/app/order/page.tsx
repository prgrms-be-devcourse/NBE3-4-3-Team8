'use client';

import React, { useState, useEffect } from 'react';
import Image from 'next/image';
import { useRouter, useSearchParams } from 'next/navigation';
import Script from 'next/script';
import { DeliveryInformationDto } from '@/app/my/types';
import DeliveryInformation from '@/app/components/order/DeliveryInformation';

// 주문 유형 enum
enum OrderType {
  CART = 'CART', // 장바구니 결제
  DIRECT = 'DIRECT', // 바로 결제
}

interface OrderItemData {
  bookId: number;
  title: string;
  coverImage: string;
  price: number;
  quantity: number;
}

interface PaymentInfo {
  cartList: OrderItemData[];
  priceStandard: number;
  pricesSales: number;
  orderId: string;
}

// API 호출 함수들
const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

/**
 * 통합된 결제 정보 조회 API
 */
const fetchPaymentInfo = async (
  orderType: OrderType = OrderType.CART,
  bookId?: number,
  quantity?: number,
): Promise<PaymentInfo> => {
  try {
    const params: any = { orderType };

    // 바로 결제인 경우 책 ID와 수량 추가
    if (orderType === OrderType.DIRECT) {
      if (!bookId || !quantity) {
        throw new Error('바로 결제 시 책 ID와 수량이 필요합니다.');
      }
      params.bookId = bookId;
      params.quantity = quantity;
    }

    const response = await fetch(
      `${API_URL}/my/orders/payment-info?` + new URLSearchParams(params),
      {
        credentials: 'include',
      },
    );

    if (!response.ok) {
      throw new Error('결제 정보 조회 실패');
    }

    return await response.json();
  } catch (error) {
    console.error('결제 정보 조회 실패:', error);
    throw error;
  }
};

/**
 * 주문 생성 API
 */
const createOrder = async (orderData: {
  postCode: string;
  fullAddress: string;
  recipient: string;
  phone: string;
  paymentMethod: string;
  bookId?: number;
  quantity?: number;
  orderType: OrderType;
  tossOrderId: String;
}) => {
  try {
    const response = await fetch(`${API_URL}/my/orders`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(orderData),
    });

    if (!response.ok) {
      throw new Error('주문 생성 실패');
    }

    return await response.json();
  } catch (error) {
    console.error('주문 생성 실패:', error);
    throw error;
  }
};

export default function OrderPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const fastOrderParam = searchParams.get('fastOrder');
  const orderItemParam = searchParams.get('orderItem');
  const bookIdParam = searchParams.get('bookId');
  const quantityParam = searchParams.get('quantity');

  // 주문 상태
  const [orderItems, setOrderItems] = useState<OrderItemData[]>([]);
  const [orderId, setOrderId] = useState<string>('');
  const [loadingPaymentInfo, setLoadingPaymentInfo] = useState(true);
  const [errorPaymentInfo, setErrorPaymentInfo] = useState<string | null>(null);

  // 가격 정보
  const [subtotal, setSubtotal] = useState(0);
  const [discount, setDiscount] = useState(0);
  const [shippingFee, setShippingFee] = useState(0);
  const [totalPrice, setTotalPrice] = useState(0);

  // 배송지 정보
  const [postCode, setPostCode] = useState('');
  const [roadAddress, setRoadAddress] = useState('');
  const [detailAddress, setDetailAddress] = useState('');
  const [recipient, setRecipient] = useState('');
  const [phone, setPhone] = useState('');

  // 토스페이먼츠 관련
  const [tossPaymentsLoaded, setTossPaymentsLoaded] = useState(false);
  const [tossWidgets, setTossWidgets] = useState<any>(null);
  const clientKey = 'test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm';
  const customerKey = 'YSZV58QAF8mLlddWuQEMN';

  // 기본 배송지 로드
  useEffect(() => {
    const storedDelivery = localStorage.getItem('defaultDelivery');
    if (storedDelivery) {
      const defaultDelivery: DeliveryInformationDto = JSON.parse(storedDelivery);
      setPostCode(defaultDelivery.postCode);
      setRoadAddress(defaultDelivery.addressName);
      setDetailAddress(defaultDelivery.detailAddress);
      setRecipient(defaultDelivery.recipient);
      setPhone(defaultDelivery.phone.replace(/-/g, ''));
    }
  }, []);

  // 주문 정보 로드
  useEffect(() => {
    const loadOrderInfo = async () => {
      try {
        setLoadingPaymentInfo(true);
        let data;

        // 빠른 주문인 경우
        if (fastOrderParam === 'true') {
          const { bookId, quantity } = getBookParams();
          if (!bookId || !quantity) return;

          data = await fetchPaymentInfo(OrderType.DIRECT, bookId, quantity);
        } else {
          data = await fetchPaymentInfo(OrderType.CART);
        }

        if (!data?.cartList?.length) {
          throw new Error('서버에서 유효한 도서 정보를 반환하지 않았습니다.');
        }

        // 결제 정보 설정
        setOrderItems(data.cartList);
        setOrderId(data.orderId);
        updatePriceInfo(data);
      } catch (error) {
        console.error('결제 정보 불러오기 실패:', error);
        setErrorPaymentInfo('결제 정보를 불러오는 데 실패했습니다.');
      } finally {
        setLoadingPaymentInfo(false);
      }
    };

    loadOrderInfo();
  }, [fastOrderParam, orderItemParam, bookIdParam, quantityParam]);

  // 토스페이먼츠 위젯 초기화
  useEffect(() => {
    if (!tossPaymentsLoaded || !totalPrice) return;

    const initTossPayments = async () => {
      try {
        // @ts-ignore - TossPayments는 외부 스크립트에서 로드됨
        const tossPayments = TossPayments(clientKey);
        const widgets = tossPayments.widgets({ customerKey });

        // 결제 금액 설정
        await widgets.setAmount({ currency: 'KRW', value: totalPrice });

        // 결제 UI 렌더링
        await Promise.all([
          widgets.renderPaymentMethods({
            selector: '#payment-method',
            variantKey: 'DEFAULT',
          }),
          widgets.renderAgreement({
            selector: '#agreement',
            variantKey: 'AGREEMENT',
          }),
        ]);

        setTossWidgets(widgets);
      } catch (error) {
        console.error('Toss Payments 초기화 오류:', error);
      }
    };

    initTossPayments();
  }, [tossPaymentsLoaded, totalPrice]);

  // bookId와 quantity 파라미터 추출
  const getBookParams = () => {
    if (orderItemParam) {
      try {
        const decodedOrderItem = JSON.parse(decodeURIComponent(orderItemParam)) as OrderItemData;
        return { bookId: decodedOrderItem.bookId, quantity: decodedOrderItem.quantity };
      } catch (error) {
        console.error('주문 정보 파싱 실패:', error);
        setErrorPaymentInfo('주문 정보를 불러오는 데 실패했습니다.');
        setLoadingPaymentInfo(false);
        return { bookId: null, quantity: null };
      }
    } else if (bookIdParam && quantityParam) {
      return {
        bookId: parseInt(bookIdParam),
        quantity: parseInt(quantityParam),
      };
    } else {
      setErrorPaymentInfo('주문 정보가 올바르지 않습니다.');
      setLoadingPaymentInfo(false);
      return { bookId: null, quantity: null };
    }
  };

  // 가격 정보 업데이트
  const updatePriceInfo = (data: PaymentInfo) => {
    const cartSubtotal = data.priceStandard;
    const cartDiscount = data.priceStandard - data.pricesSales;
    const cartShippingFee = 0; // 배송비 정책에 따라 조정

    setSubtotal(cartSubtotal);
    setDiscount(cartDiscount);
    setShippingFee(cartShippingFee);
    setTotalPrice(data.pricesSales + cartShippingFee);
  };

  // 주소 검색 스크립트 로드
  const loadPostcodeScript = () => {
    return new Promise((resolve) => {
      const script = document.createElement('script');
      script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
      script.onload = () => resolve(null);
      document.head.appendChild(script);
    });
  };

  // 주소 검색 처리
  const handleAddressSearch = async () => {
    if (!(window as any).daum || !(window as any).daum.Postcode) {
      await loadPostcodeScript();
    }
    new (window as any).daum.Postcode({
      oncomplete: function (data: any) {
        setPostCode(data.zonecode);
        setRoadAddress(data.roadAddress);
        setDetailAddress('');
        document.getElementById('detailAddress')?.focus();
      },
    }).open();
  };

  // 결제 처리
  const handlePayment = async () => {
    if (!tossWidgets) {
      alert('결제 시스템을 초기화하는 중입니다. 잠시 후 다시 시도해주세요.');
      return;
    }

    if (!postCode || !roadAddress || !recipient || !phone) {
      alert('배송 정보를 모두 입력해주세요.');
      return;
    }

    if (!orderId) {
      alert('주문 정보를 불러오는 중입니다. 잠시 후 다시 시도해주세요.');
      return;
    }

    try {
      // 주문 생성 요청
      const { bookId, quantity } =
        fastOrderParam === 'true' ? getBookParams() : { bookId: undefined, quantity: undefined };

      // 통합 API 사용하여 주문 생성
      await createOrder({
        postCode,
        fullAddress: `${roadAddress} ${detailAddress}`.trim(),
        recipient,
        phone,
        paymentMethod: 'CARD', // 기본값 설정
        bookId,
        quantity,
        orderType: fastOrderParam === 'true' ? OrderType.DIRECT : OrderType.CART,
        tossOrderId: orderId,
      });

      const orderName =
        orderItems.length > 1
          ? `${orderItems[0].title} 외 ${orderItems.length - 1}건`
          : orderItems[0].title;

      await tossWidgets.requestPayment({
        orderId,
        orderName,
        successUrl: `http://localhost:8080/order/success`,
        failUrl: `http://localhost:8080//order/fail`,
        customerName: recipient,
        customerMobilePhone: phone,
      });
    } catch (error) {
      alert('사용자가 결제를 취소했습니다.');
    }
  };

  if (loadingPaymentInfo) {
    return <div className="max-w-6xl mx-auto py-8">결제 정보를 불러오는 중...</div>;
  }

  if (errorPaymentInfo || orderItems.length === 0) {
    return (
      <div className="max-w-6xl mx-auto py-8 text-center text-red-500">
        {errorPaymentInfo || '주문 정보가 없습니다.'}
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto py-8">
      <Script
        src="https://js.tosspayments.com/v2/standard"
        onLoad={() => setTossPaymentsLoaded(true)}
      />

      <h1 className="text-2xl font-bold mb-6">주문결제</h1>
      <div className="flex flex-col md:flex-row gap-8">
        {/* 왼쪽: 배송지 등록 폼 및 결제 수단 */}
        <div className="w-full md:flex-1 space-y-6">
          <DeliveryInformation
            postCode={postCode}
            roadAddress={roadAddress}
            detailAddress={detailAddress}
            recipient={recipient}
            phone={phone}
            onSearchAddress={handleAddressSearch}
            setPostCode={setPostCode}
            setRoadAddress={setRoadAddress}
            setDetailAddress={setDetailAddress}
            setRecipient={setRecipient}
            setPhone={setPhone}
          />

          {/* 결제 위젯 */}
          <div className="border border-gray-200 rounded p-4">
            <h2 className="text-lg font-bold mb-4">결제 수단</h2>
            <div id="payment-method"></div>
            <div id="agreement" className="mt-4"></div>
          </div>
        </div>

        {/* 오른쪽: 주문 상품 목록 및 결제 금액 정보 */}
        <div className="w-full md:w-96 space-y-6">
          {/* 주문 상품 목록 */}
          <div className="border border-gray-200 rounded p-4">
            <h2 className="text-lg font-bold mb-4">주문 상품</h2>
            <div className="space-y-4">
              {orderItems.map((item, index) => (
                <div key={index} className="flex gap-4 border-b pb-4 last:border-b-0 last:pb-0">
                  <div className="w-16 h-20 relative flex-shrink-0">
                    <Image
                      src={item.coverImage || '/images/book-placeholder.png'}
                      alt={item.title}
                      fill
                      style={{ objectFit: 'cover' }}
                      className="rounded"
                    />
                  </div>
                  <div className="flex-grow">
                    <h3 className="font-medium">{item.title}</h3>
                    <div className="text-sm text-gray-500">
                      {item.quantity}권 | {item.price.toLocaleString()}원
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* 결제 금액 정보 */}
          <div className="border border-gray-200 rounded p-4">
            <h2 className="text-lg font-bold mb-4">결제 금액</h2>
            <div className="space-y-2">
              <div className="flex justify-between">
                <span>상품 금액</span>
                <span>{subtotal.toLocaleString()}원</span>
              </div>
              <div className="flex justify-between text-red-500">
                <span>할인 금액</span>
                <span>-{discount.toLocaleString()}원</span>
              </div>
              <div className="flex justify-between">
                <span>배송비</span>
                <span>{shippingFee.toLocaleString()}원</span>
              </div>
              <div className="flex justify-between items-center pt-2 border-t border-gray-200 mt-2">
                <span className="font-bold">최종 결제 금액</span>
                <span className="font-bold text-lg">{totalPrice.toLocaleString()}원</span>
              </div>

              {/* 결제하기 버튼 */}
              <button
                onClick={handlePayment}
                className="button w-full bg-black text-white py-3 rounded hover:bg-gray-800 transition-colors mt-6"
                disabled={!tossWidgets}
              >
                결제하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
