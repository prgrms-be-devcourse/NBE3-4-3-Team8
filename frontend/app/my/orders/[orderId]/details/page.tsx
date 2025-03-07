"use client";

import React, { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';

interface DetailOrder {
    orderId: string;
    bookTitle: string;
    bookQuantity: number;
    totalPrice: number;
    deliveryStatus: string;
    coverImage: string | null;
    recipient: string;
    phone: string;
    fullAddress: string;
}

export default function OrderDetailsPage() {
    const [detailOrders, setDetailOrders] = useState<DetailOrder[]>([]);
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

                const data: DetailOrder[] = await response.json();
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

            {/* 주문 상세 정보 출력 */}
            <div className="space-y-4">
                {detailOrders.map((detail) => (
                    <div key={`${detail.orderId}-${detail.bookTitle}`} className="bg-gray-50 p-4 rounded-lg shadow flex items-center space-x-4">
                        {/* 이미지 왼쪽에 배치 */}
                        {detail.coverImage && (
                            <img src={detail.coverImage} alt="Book Cover" className="w-32 h-32 object-cover" />
                        )}

                        {/* 정보 오른쪽에 배치 */}
                        <div>
                            <p><strong>주문 ID:</strong> {detail.orderId}</p>
                            <p><strong>도서 제목:</strong> {detail.bookTitle}</p>
                            <p><strong>수량:</strong> {detail.bookQuantity}</p>
                            <p><strong>총 금액:</strong> {detail.totalPrice.toLocaleString()}원</p>
                            <p><strong>배송 상태:</strong> {detail.deliveryStatus}</p>
                        </div>
                    </div>
                ))}
            </div>

            {/* 배송 정보 박스 */}
            <div className="bg-gray-50 p-4 mt-6 rounded-lg shadow">
                <h3 className="text-xl font-semibold mb-4">배송 정보</h3>
                {detailOrders.map((detail) => (
                    <div key={`${detail.orderId}-shipping`} className="space-y-2">
                        <p><strong>수령인:</strong> {detail.recipient}</p>
                        <p><strong>휴대폰:</strong> {detail.phone}</p>
                        <p><strong>주소:</strong> {detail.fullAddress}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}