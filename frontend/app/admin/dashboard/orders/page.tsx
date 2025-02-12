'use client';

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Table from '@/app/components/admin/Table';
import Pagination from '@/app/components/admin/Pagination';

const AdminOrderPage = () => {
  const [orders, setOrders] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  // 배송 상태 수정용 로컬 상태 (detailOrderId -> 현재 선택된 배송 상태)
  const [editedDetailStatuses, setEditedDetailStatuses] = useState({});

  // 주문 목록 조회 (백엔드: GET /admin/orders)
  useEffect(() => {
    fetchOrders();
  }, [currentPage]);

  const fetchOrders = async () => {
    setLoading(true);
    try {
      const response = await axios.get(
        `http://localhost:8080/admin/orders?page=${currentPage - 1}&pageSize=10`,
        { withCredentials: true },
      );
      setOrders(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error('❌ 주문 조회 실패:', error.response?.data);
    } finally {
      setLoading(false);
    }
  };

  // 상세보기 모달 열기
  const handleShowDetail = (order) => {
    setSelectedOrder(order);
    // 선택한 주문에 있는 각 상세 주문의 배송 상태를 로컬 상태에 초기화
    if (order.detailOrders) {
      const statuses = {};
      order.detailOrders.forEach((detail) => {
        statuses[detail.id] = detail.deliveryStatus;
      });
      setEditedDetailStatuses(statuses);
    }
    setIsModalOpen(true);
  };

  // 주문 삭제 (DELETE /admin/orders/{orderId})
  // const handleDeleteOrder = async (orderId) => {
  //   if (!confirm('정말 이 주문을 삭제하시겠습니까?')) return;
  //   try {
  //     const response = await axios.delete(`http://localhost:8080/my/orders/${orderId}`, {
  //       withCredentials: true,
  //     });
  //     alert(response.data);
  //     fetchOrders();
  //     setIsModalOpen(false);
  //   } catch (error) {
  //     console.error('❌ 주문 삭제 실패:', error.response?.data);
  //     alert('주문 삭제에 실패했습니다.');
  //   }
  // };

  const handleUpdateDetailStatus = async (detailId) => {
    const newStatus = editedDetailStatuses[detailId];

    try {
      const response = await axios.patch(
        `http://localhost:8080/admin/detail-orders/${detailId}/status`,
        { status: newStatus }, // JSON.stringify 불필요
        {
          withCredentials: true,
          headers: {
            'Content-Type': 'application/json', // Content-Type 추가
          },
        },
      );

      // 업데이트된 상태 반영
      setSelectedOrder((prev) => {
        if (!prev) return prev;
        const updatedDetails = prev.detailOrders.map((detail) =>
          detail.id === detailId
            ? { ...detail, deliveryStatus: response.data.deliveryStatus }
            : detail,
        );
        return { ...prev, detailOrders: updatedDetails };
      });

      alert('배송 상태가 업데이트되었습니다.');
    } catch (error) {
      console.error('배송 상태 수정 실패:', error.response?.data);
      alert('배송 상태 수정에 실패했습니다.');
    }
  };

  return (
    <div className="max-w-6xl mx-auto p-6">
      <h1 className="text-2xl font-semibold mb-4">주문 내역</h1>

      {/* 검색창 */}
      <div className="flex justify-between mb-4">
        <input
          type="text"
          placeholder="주문번호 또는 상태 검색"
          className="border p-2 rounded-md w-1/3"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* 주문 목록 테이블 및 페이징 */}
      {loading ? (
        <div className="text-center text-gray-500">주문 목록을 불러오는 중...</div>
      ) : (
        <>
          <Table
            columns={[
              { key: 'orderId', label: '주문번호' },
              { key: 'createdDate', label: '주문일시' },
              { key: 'totalPrice', label: '총 주문 금액' },
              { key: 'status', label: '주문 상태' },
              {
                key: 'detail',
                label: '',
                render: (order) => (
                  <button
                    onClick={() => handleShowDetail(order)}
                    className="bg-gray-300 px-2 py-1 rounded text-sm"
                  >
                    📖
                  </button>
                ),
              },
            ]}
            data={orders.filter(
              (order) =>
                String(order.orderId).includes(searchQuery) ||
                order.status.toLowerCase().includes(searchQuery.toLowerCase()),
            )}
          />
          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            onPageChange={setCurrentPage}
          />
        </>
      )}

      {/* 주문 상세보기 모달 */}
      {isModalOpen && selectedOrder && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center p-4 z-50">
          <div className="bg-white rounded-xl shadow-lg max-w-5xl w-full max-h-[90vh] overflow-y-auto">
            {/* 모달 헤더 */}
            <div className="flex justify-between items-center border-b p-4">
              <h2 className="text-2xl font-bold">주문 상세 정보</h2>
              <div className="flex gap-2">
                {/*<button*/}
                {/*  onClick={() => handleDeleteOrder(selectedOrder.orderId)}*/}
                {/*  className="bg-red-500 text-white px-4 py-2 rounded"*/}
                {/*>*/}
                {/*  삭제*/}
                {/*</button>*/}
                <button
                  onClick={() => {
                    setIsModalOpen(false);
                    setSelectedOrder(null);
                  }}
                  className="bg-gray-500 text-white px-4 py-2 rounded"
                >
                  닫기
                </button>
              </div>
            </div>

            {/* 모달 콘텐츠 */}
            <div className="p-4">
              <p>
                <strong>주문번호:</strong> {selectedOrder.orderId}
              </p>
              <p>
                <strong>주문일시:</strong> {new Date(selectedOrder.createdDate).toLocaleString()}
              </p>
              <p>
                <strong>총 주문 금액:</strong> {selectedOrder.totalPrice} 원
              </p>
              <p>
                <strong>주문 상태:</strong> {selectedOrder.status}
              </p>

              {/* 상세 주문 내역 및 배송 상태 수정 */}
              <div className="mt-4">
                <h3 className="text-xl font-semibold mb-2">상세 주문 내역</h3>
                {selectedOrder.detailOrders && selectedOrder.detailOrders.length > 0 ? (
                  <ul className="space-y-4">
                    {selectedOrder.detailOrders.map((detail) => (
                      <li key={detail.id} className="border p-4 rounded">
                        <p>
                          <strong>상품명:</strong> {detail.bookTitle || '정보 없음'}
                        </p>
                        <p>
                          <strong>구매 수량:</strong> {detail.bookQuantity || '정보 없음'}
                        </p>
                        <div className="flex items-center mt-2">
                          <label className="mr-2 font-semibold">배송 상태:</label>
                          <select
                            value={editedDetailStatuses[detail.id] || detail.deliveryStatus}
                            onChange={(e) =>
                              setEditedDetailStatuses({
                                ...editedDetailStatuses,
                                [detail.id]: e.target.value,
                              })
                            }
                            className="border p-1 rounded"
                          >
                            <option value="PENDING">PENDING</option>
                            <option value="SHIPPING">SHIPPING</option>
                            <option value="DELIVERED">DELIVERED</option>
                          </select>
                          <button
                            onClick={() => handleUpdateDetailStatus(detail.id)}
                            className="bg-blue-500 text-white px-3 py-1 rounded ml-4"
                          >
                            수정
                          </button>
                        </div>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p>상세 주문 내역이 없습니다.</p>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminOrderPage;
