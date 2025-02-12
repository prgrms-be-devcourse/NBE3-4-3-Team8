'use client';
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

function OrderDetails() {
  const [detailOrders, setDetailOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const { orderId } = useParams();

  useEffect(() => {
    axios
      .get(`http://localhost:8080/my/orders/${orderId}/details`, {
        withCredentials: true,
      })
      .then((response) => {
        console.log('API Response:', response.data);
        if (Array.isArray(response.data)) {
          setDetailOrders(response.data);
        } else {
          console.error('Expected an array, but received:', response.data);
          setDetailOrders([]);
        }
        setLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching order details:', error.response || error);
        setLoading(false);
      });
  }, [orderId]);

  if (loading) {
    return <div>로딩 중...</div>;
  }

  if (detailOrders.length === 0) {
    return <p>이 주문에 대한 상세 정보가 없습니다.</p>;
  }

  return (
    <div>
      <h2>주문 상세 정보</h2>
      {detailOrders.map((detail) => (
        <div key={detail.orderId}>
          <p>주문 ID: {detail.orderId}</p>
          <p>도서 ID: {detail.bookId}</p>
          <p>수량: {detail.bookQuantity}</p>
          <p>배송 상태: {detail.deliveryStatus}</p>
          <hr />
        </div>
      ))}
    </div>
  );
}

export default OrderDetails;
