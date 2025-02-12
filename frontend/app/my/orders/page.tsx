'use client';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Sidebar from '@/app/components/my/Sidebar';

export default function OrdersPage() {
  const [orders, setOrders] = useState<any[]>([]);
  const [filteredOrders, setFilteredOrders] = useState<any[]>([]);
  const [error, setError] = useState('');
  const [selectedDate, setSelectedDate] = useState<string>('');
  const router = useRouter();

  useEffect(() => {
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
        if (!Array.isArray(data)) {
          throw new Error('Invalid response format');
        }

        setOrders(data);
        setFilteredOrders(data);
      } catch (err) {
        console.error('Failed to load order list', err);
        setError('주문 목록을 불러오는 데 실패했습니다.');
      }
    };

    fetchOrders();
  }, []);

const handleDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
  const selectedDate = event.target.value;
  setSelectedDate(selectedDate);

  if (selectedDate) {
    const filtered = orders.filter(order => {
      // createDate가 유효한 날짜인지 확인
      const createDate = new Date(order.createDate);
      if (isNaN(createDate.getTime())) {
        console.error(`Invalid date format: ${order.createDate}`);
        return false; // 유효하지 않으면 필터링하지 않음
      }
      return createDate.toISOString().split('T')[0] === selectedDate;
    });
    setFilteredOrders(filtered);
  } else {
    setFilteredOrders(orders);
  }
};

  return (
    <div className="flex">
      <Sidebar />
      <main className="ml-64 p-8 w-full">
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
            <p className="text-gray-600">해당 날짜의 주문이 없습니다.</p>
          ) : (
            filteredOrders.map((order) => (
           <li
             key={order.orderId}
             className="border p-12 my-6 rounded-lg shadow-lg hover:bg-gray-200 transition-all duration-300 relative"
           >
             <div className="absolute top-2 left-2 text-sm text-gray-500">
               
               {order.createDate ?
                 new Date(order.createDate.replace(' ', 'T')).toLocaleDateString('ko-KR')
                 : '날짜 정보 없음'}
             </div>
             <p className="text-xl font-semibold">Order ID: {order.orderId}</p>
             <p className="text-xl font-semibold">Total Price: {order.totalPrice.toLocaleString()}원</p>
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