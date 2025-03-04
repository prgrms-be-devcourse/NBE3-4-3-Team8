'use client';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Sidebar from '@/app/components/my/Sidebar';

export default function OrdersPage() {
  const [orders, setOrders] = useState<any[]>([]);
  const [filteredOrders, setFilteredOrders] = useState<any[]>([]);
  const [error, setError] = useState('');
  const [selectedDate, setSelectedDate] = useState<string>('');
  const [page, setPage] = useState(0); // 현재 페이지
  const [pageSize] = useState(3);
  const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수
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
        const res = await fetch(`http://localhost:8080/my/orders?page=${page}&size=${pageSize}`, {
          method: 'GET',
          credentials: 'include',
        });

        if (!res.ok) {
          throw new Error(`HTTP error! Status: ${res.status}`);
        }

        const data = await res.json();
        if (!Array.isArray(data.content)) {
          throw new Error('Invalid response format');
        }

        setOrders(data.content); // 데이터 목록
        setTotalPages(data.totalPages); // 전체 페이지 수
        setFilteredOrders(data.content); // 필터링된 주문 목록
      } catch (err) {
        console.error('Failed to load order list', err);
        setError('주문 목록을 불러오는 데 실패했습니다.');
      }
    };

    fetchOrders();
  }, [page, pageSize]);

  const handleDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const selectedDate = event.target.value;
    setSelectedDate(selectedDate);

    if (selectedDate) {
      const filtered = orders.filter(order => {
        const createDate = new Date(order.createDate);
        if (isNaN(createDate.getTime())) {
          console.error(`Invalid date format: ${order.createDate}`);
          return false;
        }
        return createDate.toISOString().split('T')[0] === selectedDate;
      });
      setFilteredOrders(filtered);
    } else {
      setFilteredOrders(orders);
    }
  };

  const handlePageChange = (newPage: number) => {
    if (newPage < 0 || newPage >= totalPages) return;
    setPage(newPage);
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
            <p className="text-gray-600">해당 날짜의 주문이 없습니다.</p>
          ) : (
            filteredOrders.map((order) => (
              <li
                key={order.orderId}
                className="border p-6 my-4 rounded-lg shadow-lg hover:bg-gray-200 transition-all duration-300 relative max-w-lg w-full mx-auto"
              >
                <div className="absolute top-2 left-2 text-sm text-gray-500">
                  {order.createDate ? new Date(order.createDate.replace(' ', 'T')).toLocaleDateString('ko-KR') : '날짜 정보 없음'}
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

        <div className="flex justify-center mt-4">
          <button
            onClick={() => handlePageChange(page - 1)}
            disabled={page === 0}
            className="px-4 py-2 bg-gray-500 text-white rounded-lg disabled:opacity-50"
          >
            이전
          </button>
          <span className="mx-4">페이지 {page + 1} / {totalPages}</span>
          <button
            onClick={() => handlePageChange(page + 1)}
            disabled={page === totalPages - 1}
            className="px-4 py-2 bg-gray-500 text-white rounded-lg disabled:opacity-50"
          >
            다음
          </button>
        </div>
      </main>
    </div>
  );
}