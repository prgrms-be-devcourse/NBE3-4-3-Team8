// app/order/success/page.tsx
'use client';

import React, { useEffect, useState } from 'react';
import { useSearchParams, useRouter } from 'next/navigation';
import Link from 'next/link';
import { createOrder } from '@/utils/api';

const OrderSuccessPage = () => {
  // URL 쿼리 파라미터로 받은 주문 정보 (화면에 표시용)
  const [orderData, setOrderData] = useState({
    orderId: '',
    amount: 0,
    paymentKey: '',
  });
  // API를 통해 주문 생성 처리 결과 메시지 (세션 데이터 기반)
  const [processMessage, setProcessMessage] = useState<string>('');

  const searchParams = useSearchParams();
  const router = useRouter();

  // URL 쿼리 파라미터에서 주문 정보를 읽어서 화면에 표시
  useEffect(() => {
    const orderId = searchParams.get('orderId');
    const amount = searchParams.get('amount');
    const paymentKey = searchParams.get('paymentKey');

    if (orderId && amount && paymentKey) {
      setOrderData({
        orderId,
        amount: parseInt(amount, 10),
        paymentKey,
      });
    }
  }, [searchParams]);

  // 세션 스토리지에 저장된 주문 데이터를 확인하고 createOrder API 호출
  useEffect(() => {
    const processOrder = async () => {
      const storedOrderData = sessionStorage.getItem('orderData');
      if (storedOrderData) {
        try {
          const orderDataFromSession = JSON.parse(storedOrderData);
          // createOrder 호출 시, 백엔드에 인증 정보가 제대로 전달되고 있는지 확인 (withCredentials가 적용되어 있음)
          await createOrder(orderDataFromSession);
          setProcessMessage('주문이 성공적으로 생성되었습니다!');
        } catch (error) {
          console.error('주문 생성 중 오류 발생:', error);
          setProcessMessage('주문 생성 중 오류가 발생했습니다.');
        } finally {
          // 주문 생성 성공, 실패와 관계없이 세션 스토리지에서 주문 데이터 삭제
          sessionStorage.removeItem('orderData');
        }
      }
    };

    processOrder();
  }, []);

  return (
      <div className="max-w-3xl mx-auto px-4 py-16 flex flex-col items-center min-h-screen">
        {/* 성공 아이콘 */}
        <div className="w-20 h-20 bg-green-500 rounded-full flex justify-center items-center text-white text-4xl mb-6">
          ✓
        </div>

        {/* 제목 */}
        <h1 className="text-2xl md:text-3xl font-bold mb-10 text-center text-gray-800">
          결제가 성공적으로 완료되었습니다
        </h1>

        {/* 주문 정보 카드 */}
        <div className="w-full bg-gray-50 rounded-xl p-6 mb-10 shadow-md">
          <h2 className="text-xl font-semibold mb-5 pb-3 border-b border-gray-200 text-gray-800">
            주문 정보
          </h2>
          <div className="space-y-4">
            <div className="flex justify-between pb-3 border-b border-gray-200">
              <span className="font-medium text-gray-600">주문 번호</span>
              <span className="font-semibold text-gray-800">{orderData.orderId}</span>
            </div>
            <div className="flex justify-between pb-3 border-b border-gray-200">
              <span className="font-medium text-gray-600">결제 금액</span>
              <span className="font-semibold text-gray-800">
              {orderData.amount.toLocaleString()}원
            </span>
            </div>
            <div className="flex justify-between">
              <span className="font-medium text-gray-600">결제 키</span>
              <span className="font-semibold text-gray-800">{orderData.paymentKey}</span>
            </div>
          </div>
        </div>

        {/* 주문 생성 처리 메시지 */}
        {processMessage && (
            <div className="mb-6 text-lg text-center text-gray-700">
              {processMessage}
            </div>
        )}

        {/* 버튼 그룹 */}
        <div className="flex flex-col sm:flex-row gap-4 w-full sm:w-auto">
          <Link
              href="/my/orders"
              className="px-6 py-3 bg-indigo-600 text-white font-semibold rounded-lg text-center transition-all hover:bg-indigo-700 hover:shadow-md hover:-translate-y-0.5"
          >
            주문 내역 확인
          </Link>
          <Link
              href="/"
              className="px-6 py-3 bg-white text-indigo-600 font-semibold border border-indigo-600 rounded-lg text-center transition-all hover:bg-gray-50 hover:shadow-md hover:-translate-y-0.5"
          >
            쇼핑 계속하기
          </Link>
        </div>
      </div>
  );
};

export default OrderSuccessPage;
