'use client';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Sidebar from '@/app/components/Sidebar';

export default function OrdersPage() {
  const [orders, setOrders] = useState<any[]>([]); // orders 기본값을 빈 배열로 설정
  const [filteredOrders, setFilteredOrders] = useState<any[]>([]); // 날짜별 필터링된 주문 목록
  const [error, setError] = useState('');
  const [selectedDate, setSelectedDate] = useState<string>(''); // 선택된 날짜
  const router = useRouter();

  useEffect(() => {
    // 임의로 주문 데이터를 넣어줌 (테스트용)
    const testOrders = [
      {
        orderId: '12345',
        totalPrice: 25000,
        orderDate: '2025-02-10', // 주문 날짜 추가
      },
      {
        orderId: '12346',
        totalPrice: 30000,
        orderDate: '2025-02-11', // 주문 날짜 추가
      },
    ];
    setOrders(testOrders); // 임의 데이터로 orders 업데이트
    setFilteredOrders(testOrders); // 처음에는 전체 주문 목록을 표시

    // 실제 API 요청 부분
    const token = document.cookie.split('; ').find((row) => row.startsWith('accessToken='));
    const accessToken = token ? token.split('=')[1] : null;

    if (!accessToken) {
      console.log('No access token found');
      setError('Access token is missing');
      return;
    }

    fetch('http://localhost:8080/my/orders', {
      method: 'GET',
      credentials: 'include',
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`HTTP error! Status: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        setOrders(data); // 실제 데이터로 업데이트
        setFilteredOrders(data); // 전체 주문 목록을 필터링된 목록으로 초기화
      })
      .catch((err) => {
        console.error('Failed to load order list', err);
        setError('주문 목록을 불러오는 데 실패했습니다.');
      });
  }, []); // 처음에만 실행되도록 빈 배열

  // 날짜 변경 시 주문 목록을 필터링하는 함수
  const handleDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const selectedDate = event.target.value;
    setSelectedDate(selectedDate);

    if (selectedDate) {
      // 날짜별로 필터링
      const filtered = orders.filter((order) => order.orderDate === selectedDate);
      setFilteredOrders(filtered);
    } else {
      // 날짜가 없으면 전체 주문 목록 표시
      setFilteredOrders(orders);
    }
  };

  return (
    <div className="flex">
      <Sidebar />
      <main className="ml-64 p-8 w-full">
        <h1 className="text-2xl font-bold">나의 주문 내역</h1>

        <div className="my-4">
          <label htmlFor="dateFilter" className="mr-2">
            날짜별 조회:
          </label>
          <input
            id="dateFilter"
            type="date"
            value={selectedDate}
            onChange={handleDateChange}
            className="border p-2 rounded"
          />
        </div>

        <ul>
          {Array.isArray(filteredOrders) && filteredOrders.length === 0 ? (
            <p>No orders found for this date.</p>
          ) : (
            Array.isArray(filteredOrders) &&
            filteredOrders.map((order) => (
              <li
                key={order.orderId}
                className="border p-12 my-6 rounded-lg shadow-lg hover:bg-gray-200 transition-all duration-300 relative" // relative 클래스 추가
              >
                <div className="absolute top-2 left-2 text-sm text-gray-500">{order.orderDate}</div>{' '}
                {/* 주문 날짜를 왼쪽 상단에 작은 글씨로 표시 */}
                <p className="text-xl font-semibold">Order ID: {order.orderId}</p>
                <p className="text-xl font-semibold">Total Price: {order.totalPrice}원</p>
                <button
                  className="text-white bg-gradient-to-r from-indigo-500 to-indigo-700 p-3 rounded-lg shadow-lg hover:scale-105 transition-all duration-300 flex items-center justify-center space-x-2 mt-6" // 간결한 디자인
                  onClick={() => {
                    if (order.orderId) {
                      router.push(`/my/orders/${order.orderId}/details`);
                    } else {
                      console.error('Order ID is missing.');
                    }
                  }}
                >
                  <span role="img" aria-label="detail" className="text-xl">
                    🔍
                  </span>{' '}
                  {/* 이모티콘 크기 키움 */}
                  <span className="text-lg font-medium">상세 조회</span> {/* 텍스트 크기 조정 */}
                </button>
              </li>
            ))
          )}
        </ul>
      </main>
    </div>
  );
}
