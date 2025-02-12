'use client';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Sidebar from '@/app/components/my/Sidebar';

export default function OrdersPage() {
  const [orders, setOrders] = useState<any[]>([]); // orders 기본값을 빈 배열로 설정
  const [filteredOrders, setFilteredOrders] = useState<any[]>([]); // 날짜별 필터링된 주문 목록
  const [error, setError] = useState('');
  const [selectedDate, setSelectedDate] = useState<string>(''); // 선택된 날짜
  const router = useRouter();

  useEffect(() => {
//     const token = document.cookie.split('; ').find(row => row.startsWith('accessToken='));
//     const accessToken = token ? token.split('=')[1] : null;
//
//     if (!accessToken) {
//       setError('Access token is missing');
//       return;
//     }

    const fetchOrders = async () => {
      try {
        const res = await fetch('http://localhost:8080/my/orders', {
          method: 'GET',
          credentials: 'include',
        });

        if (!res.ok) {
          throw new Error(`HTTP error! Status: ${res.status}`);
        }

        const data = await res.json();
        setOrders(data);
        setFilteredOrders(data); // 전체 주문 목록을 필터링된 목록으로 초기화
      } catch (err) {
        console.error('Failed to load order list', err);
        setError('주문 목록을 불러오는 데 실패했습니다.');
      }
    };

    fetchOrders();
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
      <main className="flex-1 p-6">

        <h1 className="text-2xl font-bold">나의 주문 내역</h1>

        <div className="my-4">
          <label htmlFor="dateFilter" className="mr-2">날짜별 조회:</label>
          <input
              id="dateFilter"
              type="date"
              value={selectedDate}
              onChange={handleDateChange}
              className="border p-2 rounded"
          />
        </div>

        {error && <p className="text-red-500">{error}</p>}

        <ul>
          {filteredOrders.length === 0 ? (
              <p>No orders found for this date.</p>
          ) : (
              filteredOrders.map((order) => (
                  <li
                      key={order.orderId}
                      className="border p-12 my-6 rounded-lg shadow-lg hover:bg-gray-200 transition-all duration-300 relative"
                  >
                    <div className="absolute top-2 left-2 text-sm text-gray-500">{order.orderDate}</div>
                    <p className="text-xl font-semibold">Order ID: {order.orderId}</p>
                    <p className="text-xl font-semibold">Total Price: {order.totalPrice}원</p>
                    <button
                        className="text-white bg-gradient-to-r from-indigo-500 to-indigo-700 p-3 rounded-lg shadow-lg hover:scale-105 transition-all duration-300 flex items-center justify-center space-x-2 mt-6"
                        onClick={() => {
                          if (order.orderId) {
                            router.push(`/my/orders/${order.orderId}/details`);
                          } else {
                            console.error('Order ID is missing.');
                          }
                        }}
                    >
                      <span role="img" aria-label="detail" className="text-xl">🔍</span>
                      <span className="text-lg font-medium">상세 조회</span>
                    </button>
                  </li>
              ))
          )}
        </ul>
      </main>
    </div>
  );
}