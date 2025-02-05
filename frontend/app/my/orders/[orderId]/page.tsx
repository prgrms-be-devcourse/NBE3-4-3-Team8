import React, { useState, useEffect } from "react";

const MyOrders = () => {
  const [memberId, setMemberId] = useState<number | null>(null); // memberId를 null로 시작
  const [orders, setOrders] = useState<any[]>([]); // 응답 받을 주문 목록

  // URL에서 memberId 쿼리 파라미터를 읽는 useEffect
  useEffect(() => {
    const params = new URLSearchParams(window.location.search); // URL 쿼리 파라미터 읽기
    const id = params.get('memberId');

    if (id) {
      setMemberId(Number(id)); // memberId 상태 업데이트
    }
  }, []); // 컴포넌트가 처음 렌더링될 때 한 번만 실행

          console.log(memberId)
  // memberId가 바뀔 때마다 API 요청을 보내는 useEffect
  useEffect(() => {
    if (memberId === null) return; // memberId가 null일 때 API 요청을 하지 않음

    console.log("API 요청 memberId:", memberId);  // ✅ 요청할 memberId 확인
    const fetchOrders = async () => {
      try {
          console.log(memberId)
        const response = await fetch(`http://localhost:8080/my/orders?memberId=${memberId}`);
        const data = await response.json();
        console.log("받은 데이터:", data);  // ✅ 응답 데이터 확인
        setOrders(data); // 응답받은 주문 목록 업데이트
      } catch (error) {
        console.error("에러 발생:", error);
      }
    };

    fetchOrders();
  }, [memberId]); // memberId가 바뀔 때마다 실행

  return (
    <div>
      <h1>주문 목록</h1>

      {/* memberId 변경을 위한 입력 필드 */}
      <input
        type="number"
        value={memberId ?? ""}
        onChange={(e) => {
          const newMemberId = Number(e.target.value);
          console.log("변경된 memberId:", newMemberId);  // 상태 변경 확인
          setMemberId(newMemberId); // memberId 상태 업데이트
        }}
      />

      {/* 주문 목록 출력 */}
      <ul>
        {orders.length === 0 ? (
          <li>주문이 없습니다.</li>
        ) : (
          orders.map((order: any) => (
            <li key={order.orderId}>
              <div>주문 ID: {order.orderId}</div>
              <div>회원 ID: {order.memberId}</div>
              <div>주문 상태: {order.orderStatus}</div>
              <div>총 가격: {order.totalPrice}원</div>
            </li>
          ))
        )}
      </ul>
    </div>
  );
};

export default MyOrders;