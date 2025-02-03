"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState("");
  const router = useRouter();

  useEffect(() => {
    const memberId = 1; // 예시로 memberId를 설정
    fetch(`http://localhost:8080/my/orders?memberId=${memberId}`)
      .then((res) => {
        if (!res.ok) {
          throw new Error(`HTTP 오류! 상태: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        console.log(data);  // 응답 데이터 확인용
        setOrders(data);
      })
      .catch((err) => {
        console.error("주문 목록 불러오기 실패", err);
        setError("주문 목록을 불러오는 데 실패했습니다.");
      });
  }, []);

  // orders가 배열인지 확인
  if (error) {
    return <p>{error}</p>;
  }

  return (
    <main className="p-8">
      <h1 className="text-2xl font-bold">내 주문 목록</h1>
      <ul>
        {orders.length === 0 ? (
          <p>주문이 없습니다.</p>  // 주문이 없을 경우
        ) : (
          orders.map((order) => (
            <li key={order.orderId} className="border p-4 my-2">
              <p>주문 ID: {order.orderId}</p>
              <p>총 가격: {order.totalPrice}원</p>
              <button
                className="text-blue-500 underline"
                onClick={() => {
                  if (order.orderId) {
                    router.push(`/my/orders/${order.orderId}/details`);  // orderId 확인 후 URL로 이동
                  } else {
                    console.error("주문 ID가 없습니다.");
                  }
                }}
              >
                상세 보기
              </button>
            </li>
          ))
        )}
      </ul>
    </main>
  );
}