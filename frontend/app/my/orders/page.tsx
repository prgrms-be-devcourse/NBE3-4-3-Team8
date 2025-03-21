'use client';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Sidebar from '@/app/components/my/Sidebar';

interface Order {
  orderId: string;
  createDate: string;
  title: string;
  totalPrice: number;
  coverImage?: string;
  id: number; // bookId를 id로 변경
}

export default function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [filteredOrders, setFilteredOrders] = useState<Order[]>([]);
  const [error, setError] = useState('');
  const [selectedDate, setSelectedDate] = useState<string>('');
  const [page, setPage] = useState(0);
  const [pageSize] = useState(3);
  const [totalPages, setTotalPages] = useState(0);
  const [confirmedOrders, setConfirmedOrders] = useState<Set<string>>(new Set());
  const router = useRouter();

  useEffect(() => {
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

        const sortedOrders = data.content.sort((a: Order, b: Order) => new Date(b.createDate).getTime() - new Date(a.createDate).getTime());

        setOrders(sortedOrders);
        setTotalPages(data.totalPages);
        setFilteredOrders(sortedOrders);
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

  const handleConfirmPurchase = (orderId: string) => {
    setConfirmedOrders(prev => new Set(prev).add(orderId));
    console.log(`구매 확정: ${orderId}`);
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
                    <li key={order.orderId} className="border p-6 my-4 rounded-lg shadow-lg hover:bg-gray-200 transition-all duration-300 flex flex-col max-w-lg w-full mx-auto relative">
                      {order.createDate && (
                          <p className="text-sm text-gray-600 absolute top-2 left-2">
                            {new Date(order.createDate.replace(' ', 'T')).toLocaleDateString('ko-KR')}
                          </p>
                      )}
                      {order.coverImage && (
                          <div className="relative mb-4 flex items-center">
                            <img src={order.coverImage} alt="Book Cover" className="w-32 h-32 object-cover mr-4" />
                            <div className="flex flex-col justify-center">
                              <p className="text-xl font-semibold">책 제목: {order.title}</p>
                              <p className="text-xl font-semibold">총 금액: {order.totalPrice.toLocaleString()}원</p>
                            </div>
                          </div>
                      )}
                      <div className="flex justify-between mt-4 space-x-2">
                        <button
                            className="flex items-center justify-center w-full p-4 rounded-xl shadow-md bg-gradient-to-r from-purple-500 to-indigo-600 text-white font-semibold hover:from-indigo-600 hover:to-purple-500 transform hover:scale-105 transition-all duration-300"
                            onClick={() => {
                              if (order.orderId) {
                                router.push(`/my/orders/${order.orderId}/details`);
                              } else {
                                console.error('Order ID is missing.');
                              }
                            }}
                        >
                          🔍 <span className="ml-2">상세 조회</span>
                        </button>

                        <button
                            className="flex items-center justify-center w-full p-4 rounded-xl shadow-md bg-gradient-to-r from-blue-500 to-blue-700 text-white font-semibold hover:from-blue-700 hover:to-blue-500 transform hover:scale-105 transition-all duration-300"
                            onClick={() => {
                              if (order.id) {
                                router.push(`/books/${order.id}`);
                              } else {
                                console.error('Book ID is missing.');
                              }
                            }}
                        >
                          ✍️ <span className="ml-2">리뷰 작성</span>
                        </button>

                        {!confirmedOrders.has(order.orderId) && (
                            <button
                                className="flex items-center justify-center w-full p-4 rounded-xl shadow-md bg-gradient-to-r from-green-500 to-green-700 text-white font-semibold hover:from-green-700 hover:to-green-500 transform hover:scale-105 transition-all duration-300"
                                onClick={() => handleConfirmPurchase(order.orderId)}
                            >
                              ✅ <span className="ml-2">구매 확정</span>
                            </button>
                        )}
                      </div>
                    </li>
                ))
            )}
          </ul>
          <div className="flex justify-center mt-4">
            <button onClick={() => handlePageChange(page - 1)} disabled={page === 0} className="px-4 py-2 bg-gray-500 text-white rounded-lg disabled:opacity-50">
              이전
            </button>
            <span className="mx-4">페이지 {page + 1} / {totalPages}</span>
            <button onClick={() => handlePageChange(page + 1)} disabled={page === totalPages - 1} className="px-4 py-2 bg-gray-500 text-white rounded-lg disabled:opacity-50">
              다음
            </button>
          </div>
        </main>
      </div>
  );
}