// /frontend/app/order/success/page.tsx

'use client';

import React, { useEffect, useState } from 'react';
import { useSearchParams, useRouter } from 'next/navigation';
import Link from 'next/link';

const OrderSuccessPage = () => {
  const [orderData, setOrderData] = useState({
    orderId: '',
    amount: 0,
    paymentKey: '',
  });

  const searchParams = useSearchParams();
  const router = useRouter();

  useEffect(() => {
    // URL에서 쿼리 파라미터 추출
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
      <div className="w-full bg-white rounded-lg shadow-md p-6 mb-8">
        <h2 className="text-xl font-semibold mb-4 pb-2 border-b">주문 정보</h2>
        <div className="space-y-3">
          <div className="flex justify-between">
            <span className="text-gray-600">주문 번호</span>
            <span className="font-medium">{orderData.orderId}</span>
          </div>
          <div className="flex justify-between">
            <span className="text-gray-600">결제 금액</span>
            <span className="font-medium">{orderData.amount.toLocaleString()}원</span>
          </div>
        </div>
      </div>

      {/* 버튼 그룹 */}
      <div className="flex flex-col sm:flex-row gap-4 w-full max-w-md">
        <Link
          href="/my/orders"
          className="flex-1 bg-gray-800 text-white py-3 px-6 rounded-md text-center font-medium hover:bg-gray-700 transition-colors"
        >
          주문 내역 보기
        </Link>
        <Link
          href="/"
          className="flex-1 bg-white border border-gray-300 text-gray-800 py-3 px-6 rounded-md text-center font-medium hover:bg-gray-50 transition-colors"
        >
          홈으로 가기
        </Link>
      </div>
    </div>
  );
};

export default OrderSuccessPage;
