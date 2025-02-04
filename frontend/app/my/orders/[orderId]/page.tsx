"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";  // useRouter 사용

export default function OrderDetailPage() {
  const router = useRouter();
  const { orderId } = router.query;  // useRouter로 query를 받는 방식

  const [order, setOrder] = useState(null);
  const [error, setError] = useState("");  // error state 추가

  useEffect(() => {
    if (!orderId) {
      setError("주문 ID가 없습니다.");
      return;
    }

    fetch(`http://localhost:8080/my/orders/${orderId}/details`)
      .then((res) => {
        if (!res.ok) {
          throw new Error(`HTTP 오류! 상태: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        console.log(data);  // 응답 데이터 확인용
        setOrder(data);
      })
      .catch((err) => {
        console.error("주문 상세 정보 불러오기 실패", err);
        setError("주문 상세 정보를 불러오는 데 실패했습니다.");
      });
  }, [orderId]);

  const handleDelete = async () => {
    const confirmDelete = confirm("정말 주문을 삭제하시겠습니까?");
    if (!confirmDelete) return;

    const response = await fetch(`http://localhost:8080/my/orders/${orderId}`, {
      method: "DELETE",
    });

    if (response.ok) {
      alert("주문이 삭제되었습니다.");
      router.push("/my/orders");
    } else {
      alert("주문 삭제 실패!");
    }
  };

  if (error) {
    return <p>{error}</p>;  // 오류 메시지 표시
  }

  if (!order) return <p>로딩 중...</p>;

  return (
    <main className="p-8">
      <h1 className="text-2xl font-bold">주문 상세 정보</h1>
      <p>주문 ID: {order.orderId}</p>
      <p>총 가격: {order.totalPrice}원</p>
      <p>배송 상태: {order.deliveryStatus}</p>

      <h2 className="text-xl mt-4">상품 목록</h2>
      <ul>
        {order.books.map((book) => (
          <li key={book.bookId} className="border p-2 my-2">
            <p>책 제목: {book.title}</p>
            <p>수량: {book.quantity}개</p>
          </li>
        ))}
      </ul>

      <button
        className="bg-red-500 text-white p-2 rounded mt-4"
        onClick={handleDelete}
      >
        주문 삭제
      </button>
    </main>
  );
}