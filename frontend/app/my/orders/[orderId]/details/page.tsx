'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Sidebar from '../../../Sidebar';

interface DetailOrderDto {
  orderId: number;
  bookId: number;
  bookQuantity: number;
  deliveryStatus: string; // DeliveryStatus를 문자열로 표시
}

export default function OrderDetailsPage() {
  const [orderDetails, setOrderDetails] = useState<DetailOrderDto[]>([
    { orderId: 1, bookId: 101, bookQuantity: 2, deliveryStatus: 'Pending' },
    { orderId: 2, bookId: 102, bookQuantity: 1, deliveryStatus: 'Shipped' },
  ]); // 임시 초기 데이터
  const [error, setError] = useState('');
  const router = useRouter();

  const [orderId, setOrderId] = useState<string | undefined>(undefined);

  // router.query가 변경될 때마다 orderId 설정
  useEffect(() => {
    if (router.query?.orderId) {  // router.query가 정의되고 orderId가 있을 때만 처리
      setOrderId(router.query.orderId as string);
    }
  }, [router.query]);

  useEffect(() => {
    if (!orderId) return; // orderId가 없으면 API 호출하지 않음

    const oauthId = 'testOauthId'; // 예시 oauthId, 실제로는 로그인한 유저의 oauthId를 가져와야 함

    fetch(`http://localhost:8080/my/orders/${orderId}/details?oauthId=${oauthId}`)
      .then((res) => {
        if (!res.ok) {
          throw new Error(`HTTP error! Status: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        setOrderDetails(data); // 주문 상세 데이터 저장
      })
      .catch((err) => {
        console.error('Failed to load order details', err);
        setError('주문 상세 정보를 불러오는 데 실패했습니다.');
      });
  }, [orderId]); // orderId가 변경될 때마다 실행

  return (
    <div className="flex">
      <Sidebar />
      <main className="ml-64 p-8 w-full">
        <h1 className="text-2xl font-bold">주문 상세</h1>
        {error && <p className="text-red-500">{error}</p>}
        {orderDetails.length > 0 ? (
          <div>
            {orderDetails.map((detail) => (
              <div
                key={detail.orderId}
                className="border p-12 my-6 rounded-lg shadow-lg hover:bg-gray-200 transition-all duration-300" // OrdersPage와 동일한 스타일
              >
                <p className="text-xl font-semibold">Order ID: {detail.orderId}</p>
                <p className="text-xl font-semibold">Book ID: {detail.bookId}</p>
                <p className="text-xl font-semibold">Book Quantity: {detail.bookQuantity}</p>
                <p className="text-xl font-semibold">Delivery Status: {detail.deliveryStatus}</p>
              </div>
            ))}
          </div>
        ) : (
          <p>Loading...</p>
        )}
      </main>
    </div>
  );
}