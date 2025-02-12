'use client';

import React, { useState, useEffect } from 'react';
import Image from 'next/image';
import { useRouter, useSearchParams } from 'next/navigation';
import { fetchPaymentInfo, createOrder, createFastOrder } from '@/utils/api';
import Script from 'next/script';

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
}

export default function OrderPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const fastOrderParam = searchParams.get('fastOrder');
  const orderItemParam = searchParams.get('orderItem');

  // 바로구매일 경우 URL에서 전달된 주문 항목 저장
  const [fastOrderItem, setFastOrderItem] = useState<OrderItemData | null>(null);
  // 일반 주문 시 사용할 결제 정보 상태
  const [paymentInfo, setPaymentInfo] = useState<PaymentInfo | null>(null);
  const [loadingPaymentInfo, setLoadingPaymentInfo] = useState(true);
  const [errorPaymentInfo, setErrorPaymentInfo] = useState<string | null>(null);

  // 배송지 등록 폼 상태
  const [postCode, setPostCode] = useState('');
  const [roadAddress, setRoadAddress] = useState('');
  const [detailAddress, setDetailAddress] = useState('');
  const [recipient, setRecipient] = useState('');
  const [phone, setPhone] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('CARD'); // 예: CARD, TOSS 등

  useEffect(() => {
    if (fastOrderParam === 'true' && orderItemParam) {
      // 바로구매인 경우 URL의 orderItem 파라미터를 파싱하여 상태에 저장
      try {
        const parsedItem: OrderItemData = JSON.parse(decodeURIComponent(orderItemParam));
        setFastOrderItem(parsedItem);
      } catch (error) {
        console.error('orderItem 파싱 오류:', error);
        setErrorPaymentInfo('주문 정보를 불러오는 데 실패했습니다.');
      } finally {
        setLoadingPaymentInfo(false);
      }
    } else {
      // 일반 결제인 경우 백엔드에서 결제 정보를 불러옴
      const loadPaymentInfo = async () => {
        try {
          const data = await fetchPaymentInfo();
          setPaymentInfo(data);
        } catch (error) {
          console.error('결제 정보 불러오기 실패:', error);
          setErrorPaymentInfo('결제 정보를 불러오는 데 실패했습니다.');
        } finally {
          setLoadingPaymentInfo(false);
        }
      };
      loadPaymentInfo();
    }
  }, [fastOrderParam, orderItemParam]);

  if (loadingPaymentInfo) {
    return <div className="max-w-6xl mx-auto py-8">결제 정보를 불러오는 중...</div>;
  }
  if (errorPaymentInfo || (!fastOrderItem && !paymentInfo)) {
    return (
        <div className="max-w-6xl mx-auto py-8 text-center text-red-500">{errorPaymentInfo}</div>
    );
  }

  // 가격 및 주문 항목 계산
  let subtotal = 0;
  let discount = 0;
  let shippingFee = 0;
  let totalPrice = 0;
  let orderItems: OrderItemData[] = [];

  if (fastOrderParam === 'true' && fastOrderItem) {
    // 바로구매인 경우: 단일 도서 정보 사용
    subtotal = fastOrderItem.price * fastOrderItem.quantity;
    discount = 0; // 직접구매는 할인 없이 진행할 수 있음
    shippingFee = 0; // 배송비 계산은 별도 처리 가능
    totalPrice = subtotal + shippingFee;
    orderItems = [fastOrderItem];
  } else if (paymentInfo) {
    // 일반 주문인 경우: 백엔드에서 받아온 결제 정보 사용
    subtotal = paymentInfo.priceStandard;
    discount = paymentInfo.priceStandard - paymentInfo.pricesSales;
    shippingFee = 0;
    totalPrice = paymentInfo.pricesSales + shippingFee;
    orderItems = paymentInfo.cartList;
  }

  // "결제하기" 버튼 클릭 시 주문 생성
  const handlePayment = async () => {
    // fullAddress 생성 (도로명주소 + 상세주소)
    const fullAddress = roadAddress.trim() + ' ' + detailAddress.trim();

    try {
      // OrderRequestDto 형식의 데이터 생성
      const orderData = {
        postCode,
        fullAddress,
        recipient,
        phone,
        paymentMethod,
      };
      if (fastOrderParam === 'true' && fastOrderItem) {
        // 바로구매인 경우 createFastOrder API 호출: 추가 인자로 bookId와 quantity를 전달
        await createFastOrder(orderData, fastOrderItem.bookId, fastOrderItem.quantity);
      } else {
        // 일반 주문인 경우 createOrder API 호출
        await createOrder(orderData);
      }
      alert('주문이 성공적으로 생성되었습니다!');
      // 주문 생성 후 후속 처리(예: 장바구니 초기화) 후 메인 페이지 등으로 이동
      router.push('/');
    } catch (error) {
      console.error('주문 생성 중 오류:', error);
      alert('주문 생성 중 오류가 발생했습니다.');
    }
  };

  // 다음 주소찾기 API 스크립트 로드
  const loadPostcodeScript = () => {
    return new Promise((resolve) => {
      const script = document.createElement('script');
      script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
      script.onload = () => resolve(null);
      document.head.appendChild(script);
    });
  };

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

  return (
      <div className="max-w-6xl mx-auto py-8">
        <h1 className="text-2xl font-bold mb-6">주문결제</h1>

        {orderItems.length === 0 ? (
            <div className="text-center py-10">주문할 상품이 없습니다.</div>
        ) : (
            <div className="flex flex-col md:flex-row gap-8">
              {/* 왼쪽: 주문 상품 목록 */}
              <div className="flex-1 space-y-6">
                {orderItems.map((item) => (
                    <div
                        key={item.bookId}
                        className="flex items-center gap-6 p-6 border border-gray-200 rounded-lg"
                    >
                      <div className="w-32 h-32 bg-gray-100 relative flex-shrink-0">
                        <Image
                            src={item.coverImage}
                            alt={item.title}
                            fill
                            className="rounded-lg object-cover"
                        />
                      </div>
                      <div className="flex-1">
                        <h3 className="text-lg font-medium">{item.title}</h3>
                        <p className="text-gray-500">가격: {item.price.toLocaleString()}원</p>
                        <p className="text-gray-500">수량: {item.quantity}</p>
                      </div>
                    </div>
                ))}
              </div>

              {/* 오른쪽: 배송지 등록 폼 & 결제 요약 */}
              <div className="w-full md:w-96 space-y-6">
                {/* 배송 정보 입력 폼 */}
                <div className="border border-gray-200 rounded p-4">
                  <h2 className="text-lg font-bold mb-4">배송 정보</h2>
                  <div className="mb-3">
                    <label className="block text-sm font-medium mb-1">주소</label>
                    <div className="flex gap-2">
                      <input
                          type="text"
                          value={postCode}
                          onChange={(e) => setPostCode(e.target.value)}
                          className="border p-2 flex-grow"
                          placeholder="우편번호"
                          readOnly
                          disabled
                          required
                      />
                      <button
                          type="button"
                          onClick={handleAddressSearch}
                          className="bg-blue-500 text-white px-4 py-2 rounded"
                      >
                        주소찾기
                      </button>
                    </div>
                  </div>
                  <div className="mb-3">
                    <input
                        type="text"
                        value={roadAddress}
                        onChange={(e) => setRoadAddress(e.target.value)}
                        className="border p-2 w-full"
                        placeholder="도로명주소"
                        readOnly
                        disabled
                        required
                    />
                  </div>
                  <div className="mb-3">
                    <input
                        type="text"
                        id="detailAddress"
                        value={detailAddress}
                        onChange={(e) => setDetailAddress(e.target.value)}
                        className="border p-2 w-full"
                        placeholder="상세주소"
                    />
                  </div>

                  <div className="mb-3">
                    <label className="block text-sm font-medium mb-1">수령인</label>
                    <input
                        type="text"
                        value={recipient}
                        onChange={(e) => setRecipient(e.target.value)}
                        className="border p-2 w-full"
                        placeholder="예) 홍길동"
                        required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="block text-sm font-medium mb-1">전화번호</label>
                    <input
                        type="text"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        className="border p-2 w-full"
                        placeholder="예) 010-1234-5678"
                        required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="block text-sm font-medium mb-1">결제수단</label>
                    <div className="flex gap-4 mt-1">
                      <label className="flex items-center gap-1">
                        <input
                            type="radio"
                            name="paymentMethod"
                            value="CARD"
                            checked={paymentMethod === 'CARD'}
                            onChange={(e) => setPaymentMethod(e.target.value)}
                        />
                        신용카드
                      </label>
                      <label className="flex items-center gap-1">
                        <input
                            type="radio"
                            name="paymentMethod"
                            value="TOSS"
                            checked={paymentMethod === 'TOSS'}
                            onChange={(e) => setPaymentMethod(e.target.value)}
                        />
                        토스
                      </label>
                    </div>
                  </div>
                </div>
                {/* 결제 요약 */}
                <div className="border border-gray-200 rounded p-4">
                  <h2 className="text-lg font-medium mb-4">결제 요약</h2>
                  <div className="flex justify-between mb-2">
                    <span>상품 금액</span>
                    <span>{subtotal.toLocaleString()}원</span>
                  </div>
                  <div className="flex justify-between mb-2">
                    <span>할인</span>
                    <span>-{discount.toLocaleString()}원</span>
                  </div>
                  <div className="flex justify-between mb-2">
                    <span>배송비</span>
                    <span>{shippingFee.toLocaleString()}원</span>
                  </div>
                  <hr className="my-3" />
                  <div className="flex justify-between mb-4 font-bold text-lg">
                    <span>총 결제 금액</span>
                    <span className="text-red-600">{totalPrice.toLocaleString()}원</span>
                  </div>
                  <button
                      onClick={handlePayment}
                      className="w-full bg-black text-white py-3 rounded hover:bg-gray-800 transition-colors"
                  >
                    결제하기
                  </button>
                </div>
              </div>
            </div>
        )}
      </div>
  );
}
