"use client";

import React, { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';

interface OrderDetail {
    orderId: string;
    bookId: string;
    bookQuantity: number;
    deliveryStatus: string;
}

export default function OrderDetailsPage() {
    const [detailOrders, setDetailOrders] = useState<OrderDetail[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const params = useParams();
    const orderId = params.orderId as string;

    useEffect(() => {
        const fetchOrderDetails = async () => {
            try {
                const response = await fetch(`http://localhost:8080/my/orders/${orderId}/details`, {
                    credentials: 'include',
                });

                if (!response.ok) {
                    throw new Error('주문 상세 정보를 불러오지 못했습니다');
                }

                const data: OrderDetail[] = await response.json();
                setDetailOrders(data);
            } catch (err) {
                setError(err instanceof Error ? err.message : '알 수 없는 오류가 발생했습니다');
            } finally {
                setLoading(false);
            }
        };

        fetchOrderDetails();
    }, [orderId]);

    if (loading) return <p className="text-center mt-8">로딩 중...</p>;
    if (error) return <p className="text-center mt-8 text-red-500">{error}</p>;
    if (detailOrders.length === 0) return <p className="text-center mt-8">이 주문에 대한 상세 정보가 없습니다.</p>;

    return (
        <div className="max-w-7xl mx-auto px-4 py-8 min-h-screen bg-white">
            <h2 className="text-2xl font-bold mb-6 text-center">주문 상세 정보</h2>
            <div className="space-y-4">
                {detailOrders.map((detail) => (
                    <div key={`${detail.orderId}-${detail.bookId}`} className="bg-gray-50 p-4 rounded-lg shadow">
                        <p><strong>주문 ID:</strong> {detail.orderId}</p>
                        <p><strong>도서 ID:</strong> {detail.bookId}</p>
                        <p><strong>수량:</strong> {detail.bookQuantity}</p>
                        <p><strong>배송 상태:</strong> {detail.deliveryStatus}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}
