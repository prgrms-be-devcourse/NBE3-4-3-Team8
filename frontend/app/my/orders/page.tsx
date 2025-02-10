'use client';
import { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';

export default function OrdersPage() {
  const searchParams = useSearchParams();
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState('');
  const router = useRouter();

  useEffect(() => {
    const memberId = searchParams.get("memberId");
    if (!memberId) return;
    fetch(`http://localhost:8080/my/orders?memberId=${memberId}`, {
      credentials: 'include',
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`HTTP error! Status: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        console.log(data); // Check response data
        setOrders(data);
      })
      .catch((err) => {
        console.error('Failed to load order list', err);
        setError('Failed to load order list.');
      });
  }, [searchParams]);

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <main className="p-8">
      <h1 className="text-2xl font-bold">My Orders</h1>
      <ul>
        {orders.length === 0 ? (
          <p>No orders found.</p>
        ) : (
          orders.map((order) => (
            <li key={order.orderId} className="border p-4 my-2">
              <p>Order ID: {order.orderId}</p>
              <p>Total Price: {order.totalPrice}원</p>
              <button
                className="text-blue-500 underline"
                onClick={() => {
                  if (order.orderId) {
                    router.push(`/my/orders/${order.orderId}/details`);
                  } else {
                    console.error('Order ID is missing.');
                  }
                }}
              >
                View Details
              </button>
            </li>
          ))
        )}
      </ul>
    </main>
  );
}