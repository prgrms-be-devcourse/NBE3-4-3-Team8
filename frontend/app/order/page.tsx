'use client';

import React, { useState, useEffect } from 'react';
import Image from 'next/image';
import { useRouter, useSearchParams } from 'next/navigation';
import { fetchPaymentInfo, fetchFastOrderInfo, fetchSinglePaymentInfo } from '@/utils/api';
import Script from 'next/script';
import { DeliveryInformationDto } from '@/app/my/types';
import DeliveryInformation from '@/app/components/order/DeliveryInformation';

interface OrderItemData {
  bookId: number;
  title: string;
  coverImage: string;
  price: number; // 판매가
  quantity: number;
}

interface PaymentInfo {
  cartList: OrderItemData[];
  priceStandard: number; // 원래 가격 합계
  pricesSales: number; // 할인 적용된 가격 합계
  orderId: string; // 백엔드에서 생성한 주문 ID
}

interface FastOrderInfo {
  bookInfo: OrderItemData;
  priceStandard: number; // 원래 가격
  pricesSales: number; // 할인 적용된 가격
  orderId: string; // 백엔드에서 생성한 주문 ID
}

export default function OrderPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const fastOrderParam = searchParams.get('fastOrder');
  const orderItemParam = searchParams.get('orderItem');
  const bookIdParam = searchParams.get('bookId');
  const quantityParam = searchParams.get('quantity');

  const [fastOrderItem, setFastOrderItem] = useState<OrderItemData | null>(null);
  const [paymentInfo, setPaymentInfo] = useState<PaymentInfo | null>(null);
  const [loadingPaymentInfo, setLoadingPaymentInfo] = useState(true);
  const [errorPaymentInfo, setErrorPaymentInfo] = useState<string | null>(null);
  const [orderId, setOrderId] = useState<string>(''); // 백엔드에서 받은 주문 ID 저장

  // 배송지 입력 폼 상태
  const [postCode, setPostCode] = useState('');
  const [roadAddress, setRoadAddress] = useState('');
  const [detailAddress, setDetailAddress] = useState('');
  const [recipient, setRecipient] = useState('');
  const [phone, setPhone] = useState('');

  // 결제 관련 상태
  const [tossPaymentsLoaded, setTossPaymentsLoaded] = useState(false);
  const [tossWidgets, setTossWidgets] = useState<any>(null);

  // 결제 정보 계산
  const [subtotal, setSubtotal] = useState(0);
  const [discount, setDiscount] = useState(0);
  const [shippingFee, setShippingFee] = useState(0);
  const [totalPrice, setTotalPrice] = useState(0);
  const [orderItems, setOrderItems] = useState<OrderItemData[]>([]);
  const [paymentMethod, setPaymentMethod] = useState('CARD');

  // Toss Payments 설정
  const clientKey = 'test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm';
  const customerKey = 'YSZV58QAF8mLlddWuQEMN';

  // Toss Payments 스크립트 로드 확인
  const handleTossPaymentsLoad = () => {
    setTossPaymentsLoaded(true);
  };

  // 결제 위젯 초기화
  useEffect(() => {
    if (!tossPaymentsLoaded || !totalPrice) return;

    const initTossPayments = async () => {
      try {
        // @ts-ignore - TossPayments는 외부 스크립트에서 로드됨
        const tossPayments = TossPayments(clientKey);

        // 회원 결제 위젯 초기화
        const widgets = tossPayments.widgets({
          customerKey,
        });

        // 결제 금액 설정
        await widgets.setAmount({
          currency: 'KRW',
          value: totalPrice,
        });

        // 결제 UI 및 이용약관 UI 렌더링
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
  }, [tossPaymentsLoaded, totalPrice, clientKey, customerKey]);

  // 마이페이지에서 저장한 기본 배송지가 있다면 읽어와서 초기값 설정
  useEffect(() => {
    const storedDelivery = localStorage.getItem('defaultDelivery');
    if (storedDelivery) {
      const defaultDelivery: DeliveryInformationDto = JSON.parse(storedDelivery);
      setPostCode(defaultDelivery.postCode);
      setRoadAddress(defaultDelivery.addressName);
      setDetailAddress(defaultDelivery.detailAddress);
      setRecipient(defaultDelivery.recipient);
      setPhone(defaultDelivery.phone);
    }
  }, []);

  // 주문 정보 로드 - 일반 결제와 빠른 주문 모두 처리
  useEffect(() => {
    const loadOrderInfo = async () => {
      try {
        setLoadingPaymentInfo(true);

        // 빠른 주문인 경우
        if (fastOrderParam === 'true') {
          let bookId: number;
          let quantity: number;

          // orderItemParam이 있는 경우 (BookInfo.tsx에서 넘어온 경우)
          if (orderItemParam) {
            try {
              // JSON 파싱하여 bookId와 quantity 추출
              const decodedOrderItem = JSON.parse(
                decodeURIComponent(orderItemParam),
              ) as OrderItemData;
              bookId = decodedOrderItem.bookId;
              quantity = decodedOrderItem.quantity;
            } catch (error) {
              console.error('주문 정보 파싱 실패:', error);
              setErrorPaymentInfo('주문 정보를 불러오는 데 실패했습니다.');
              setLoadingPaymentInfo(false);
              return;
            }
          }
          // bookId와 quantity 파라미터가 직접 제공된 경우
          else if (bookIdParam && quantityParam) {
            bookId = parseInt(bookIdParam);
            quantity = parseInt(quantityParam);
          } else {
            setErrorPaymentInfo('주문 정보가 올바르지 않습니다.');
            setLoadingPaymentInfo(false);
            return;
          }

          // 백엔드에서 빠른 주문 정보 가져오기
          console.log('Fetching single payment info for bookId:', bookId, 'quantity:', quantity);
          const data = await fetchSinglePaymentInfo(bookId, quantity);
          console.log('Received book info:', data);

          if (!data || !data.cartList || data.cartList.length === 0) {
            throw new Error('서버에서 유효한 도서 정보를 반환하지 않았습니다.');
          }

          setFastOrderItem(data.cartList[0]);

          // 결제 정보 계산
          const itemSubtotal = data.priceStandard;
          const itemDiscount = data.priceStandard - data.pricesSales;
          const itemShippingFee = 0; // 배송비 정책에 따라 조정

          setSubtotal(itemSubtotal);
          setDiscount(itemDiscount);
          setShippingFee(itemShippingFee);
          setTotalPrice(data.pricesSales + itemShippingFee);
          setOrderItems(data.cartList);

          // 백엔드에서 생성한 주문 ID 저장
          setOrderId(data.orderId);
        }
        // 일반 결제인 경우 (장바구니)
        else {
          const data = await fetchPaymentInfo();
          setPaymentInfo(data);

          // 백엔드에서 생성한 주문 ID 저장
          setOrderId(data.orderId);

          // 결제 정보 계산
          const cartSubtotal = data.priceStandard;
          const cartDiscount = data.priceStandard - data.pricesSales;
          const cartShippingFee = 0; // 배송비 정책에 따라 조정

          setSubtotal(cartSubtotal);
          setDiscount(cartDiscount);
          setShippingFee(cartShippingFee);
          setTotalPrice(data.pricesSales + cartShippingFee);
          setOrderItems(data.cartList);
        }
      } catch (error) {
        console.error('결제 정보 불러오기 실패:', error);
        setErrorPaymentInfo('결제 정보를 불러오는 데 실패했습니다.');
      } finally {
        setLoadingPaymentInfo(false);
      }
    };

    loadOrderInfo();
  }, [fastOrderParam, orderItemParam, bookIdParam, quantityParam]);

  // 주소 검색 스크립트 로드
  const loadPostcodeScript = () => {
    return new Promise((resolve) => {
      const script = document.createElement('script');
      script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
      script.onload = () => resolve(null);
      document.head.appendChild(script);
    });
  };

  // 주소 검색 처리 (DeliveryInformation 컴포넌트에 전달)
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

    const fullAddress = roadAddress.trim() + ' ' + detailAddress.trim();

    try {
      // 백엔드에서 받은 주문 ID 사용
      const orderName =
        orderItems.length > 1
          ? `${orderItems[0].title} 외 ${orderItems.length - 1}건`
          : orderItems[0].title;

      await tossWidgets.requestPayment({
        orderId: orderId, // 백엔드에서 받은 주문 ID 사용
        orderName: orderName,
        successUrl: `http://localhost:8080/order/success`,
        failUrl: `${window.location.origin}/order/fail`,
        customerEmail: '', // 필요시 추가
        customerName: recipient,
        customerMobilePhone: phone,
      });
      // 주문 생성 로직은 successUrl 페이지에서 처리
    } catch (error) {
      console.error('결제 처리 중 오류:', error);
      alert('결제 처리 중 오류가 발생했습니다.');
    }
  };

  if (loadingPaymentInfo) {
    return <div className="max-w-6xl mx-auto py-8">결제 정보를 불러오는 중...</div>;
  }

  if (errorPaymentInfo || (!fastOrderItem && !paymentInfo)) {
    return (
      <div className="max-w-6xl mx-auto py-8 text-center text-red-500">{errorPaymentInfo}</div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto py-8">
      {/* Toss Payments 스크립트 로드 */}
      <Script src="https://js.tosspayments.com/v2/standard" onLoad={handleTossPaymentsLoad} />

      <h1 className="text-2xl font-bold mb-6">주문결제</h1>
      {orderItems.length === 0 ? (
        <div className="text-center py-10">주문할 상품이 없습니다.</div>
      ) : (
        <div className="flex flex-col md:flex-row gap-8">
          {/* 왼쪽: 배송지 등록 폼 및 결제 수단 */}
          <div className="w-full md:flex-1 space-y-6">
            {/* 배송정보 컴포넌트 사용 */}
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
                  className="button w-full bg-black text-white py-3 rounded hover:bg-gray-800 transition-colors"
                  id="payment-button"
                  style={{ marginTop: '30px' }}
                  disabled={!tossWidgets}
                >
                  결제하기
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
