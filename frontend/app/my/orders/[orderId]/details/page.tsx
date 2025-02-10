"use client";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";

export default function OrderDetailPage() {
  const { orderId } = useParams();
  const [order, setOrder] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!orderId) return;

    fetch(`http://localhost:8080/my/orders/${orderId}/details`)
      .then((res) => {
        if (!res.ok) {
          throw new Error(`HTTP 오류! 상태: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        console.log("받은 데이터:", data); // 개발자 도구에서 확인
        if (Array.isArray(data) && data.length > 0) {
          setOrder(data[0]); // 배열이면 첫 번째 요소 사용
        } else {
          setError("주문 정보를 찾을 수 없습니다.");
        }
      })
      .catch((err) => {
        console.error("주문 상세 정보 불러오기 실패", err);
        setError("주문 상세 정보를 불러오는 데 실패했습니다.");
      });
  }, [orderId]);

  if (error) {
    return <p>{error}</p>;
  }

  if (!order) {
    return <p>로딩 중...</p>;
  }

  return (
    <main className="p-8">
      <h1 className="text-2xl font-bold">주문 상세 정보</h1>
      <p>주문 ID: {order.orderId}</p>
      <p>책 ID: {order.bookId}</p>
      <p>책 수량: {order.bookQuantity}</p>
      <p>배송 상태: {order.deliveryStatus}</p>
    </main>
  );
}