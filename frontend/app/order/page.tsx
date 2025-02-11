'use client';
import React, { useState, useEffect } from 'react';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import { fetchPaymentInfo, createOrder } from '@/utils/api';
import Script from 'next/script';

interface OrderItemData {
    bookId: number;
    title: string;
    coverImage: string;
    price: number; // 판매가
    quantity: number;
}

interface PaymentInfo {
    cartList: OrderItemData[];
    priceStandard: number; // 원래 가격 합계
    pricesSales: number; // 할인 적용된 가격 합계
}

export default function OrderPage() {
    const router = useRouter();

    // 결제 정보 (카트 항목, 가격 정보 등)를 백엔드에서 받아옴
    const [paymentInfo, setPaymentInfo] = useState<PaymentInfo | null>(null);
    const [loadingPaymentInfo, setLoadingPaymentInfo] = useState(true);
    const [errorPaymentInfo, setErrorPaymentInfo] = useState<string | null>(null);

    // 배송지 등록 폼 상태
    const [postCode, setPostCode] = useState('');
    const [roadAddress, setRoadAddress] = useState('');
    const [detailAddress, setDetailAddress] = useState('');
    const [recipient, setRecipient] = useState('');
    const [phone, setPhone] = useState('');
    const [paymentMethod, setPaymentMethod] = useState('CARD'); // 예: CARD, TOSS, ...

    // 배송지 입력 등은 그대로 사용

    // 결제 정보 불러오기
    useEffect(() => {
        const loadPaymentInfo = async () => {
            try {
                const data = await fetchPaymentInfo();
                setPaymentInfo(data);
            } catch (error) {
                setErrorPaymentInfo('결제 정보를 불러오는 데 실패했습니다.');
            } finally {
                setLoadingPaymentInfo(false);
            }
        };
        loadPaymentInfo();
    }, []);

    // 결제 정보가 없을 경우 처리
    if (loadingPaymentInfo) {
        return <div className="max-w-6xl mx-auto py-8">결제 정보를 불러오는 중...</div>;
    }
    if (errorPaymentInfo || !paymentInfo) {
        return (
            <div className="max-w-6xl mx-auto py-8 text-center text-red-500">{errorPaymentInfo}</div>
        );
    }

    // 배송비와 할인은 (예시로) 배송비는 0, 할인은 원래 가격과 할인 가격 차이로 계산
    const shippingFee = 0;
    const discount = paymentInfo.priceStandard - paymentInfo.pricesSales;
    const subtotal = paymentInfo.priceStandard;
    const totalPrice = paymentInfo.pricesSales + shippingFee; // 실제 결제 금액

    // 수량 변경 및 삭제는 결제 정보(cartList)를 직접 수정하지 않고,
    // 주문 페이지에서는 결제 정보(백엔드에서 계산한 값)를 보여주기만 함.
    // (만약 수정 기능이 필요하다면 별도의 로직이 필요합니다.)

    // "결제하기" 버튼 클릭 시 주문 생성 API 호출
    const handlePayment = async () => {
        // fullAddress 생성 (도로명 + 상세주소)
        const fullAddress = roadAddress.trim() + ' ' + detailAddress.trim();

        try {
            const orderData = {
                postCode,
                fullAddress,
                recipient,
                phone,
                paymentMethod,
            };
            const data = await createOrder(orderData);
            alert('주문이 성공적으로 생성되었습니다!');
            // 주문 생성 후 필요한 후속 처리(예: 장바구니 초기화)
            router.push('/'); // 주문 완료 페이지로 이동
        } catch (error) {
            console.error('주문 생성 중 오류:', error);
            alert('주문 생성 중 오류가 발생했습니다.');
        }
    };

    const loadPostcodeScript = () => {
        return new Promise((resolve) => {
            const script = document.createElement('script');
            script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
            script.onload = () => resolve();
            document.head.appendChild(script);
        });
    };

    const handleAddressSearch = async () => {
        if (!window.daum || !window.daum.Postcode) {
            await loadPostcodeScript();
        }
        new window.daum.Postcode({
            oncomplete: function (data) {
                setPostCode(data.zonecode);
                setRoadAddress(data.roadAddress);
                setDetailAddress('');
                document.getElementById('detailAddress')?.focus();
            },
        }).open();
    };

    return (
        <div className="max-w-6xl mx-auto py-8">
            <h1 className="text-2xl font-bold mb-6">주문결제</h1>

            {paymentInfo.cartList.length === 0 ? (
                <div className="text-center py-10">주문할 상품이 없습니다.</div>
            ) : (
                <div className="flex flex-col md:flex-row gap-8">
                    {/* 왼쪽: 주문 상품 목록 */}
                    <div className="flex-1 space-y-6">
                        {paymentInfo.cartList.map((item) => (
                            <div
                                key={item.bookId}
                                className="flex items-center gap-6 p-6 border border-gray-200 rounded-lg"
                            >
                                <div className="w-32 h-32 bg-gray-100 relative flex-shrink-0">
                                    <Image
                                        src={item.coverImage}
                                        alt={item.title}
                                        fill
                                        className="rounded-lg object-cover"
                                    />
                                </div>
                                <div className="flex-1">
                                    <h3 className="text-lg font-medium">{item.title}</h3>
                                    <p className="text-gray-500">가격: {item.price.toLocaleString()}원</p>
                                    <p className="text-gray-500">수량: {item.quantity}</p>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* 오른쪽: 배송지 등록 폼 & 결제 요약 */}
                    <div className="w-full md:w-96 space-y-6">
                        {/* 배송 정보 입력 폼 */}
                        <div className="border border-gray-200 rounded p-4">
                            <h2 className="text-lg font-bold mb-4">배송 정보</h2>
                            <div className="mb-3">
                                <label className="block text-sm font-medium mb-1">주소</label>
                                <div className="flex gap-2">
                                    <input
                                        type="text"
                                        value={postCode}
                                        onChange={(e) => setPostCode(e.target.value)}
                                        className="border p-2 flex-grow"
                                        placeholder="우편번호"
                                        readOnly
                                        disabled
                                        required
                                    />
                                    <button
                                        type="button"
                                        onClick={handleAddressSearch}
                                        className="bg-blue-500 text-white px-4 py-2 rounded"
                                    >
                                        주소찾기
                                    </button>
                                </div>
                            </div>
                            <div className="mb-3">
                                <input
                                    type="text"
                                    value={roadAddress}
                                    onChange={(e) => setRoadAddress(e.target.value)}
                                    className="border p-2 w-full"
                                    placeholder="도로명주소"
                                    readOnly
                                    disabled
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <input
                                    type="text"
                                    id="detailAddress"
                                    value={detailAddress}
                                    onChange={(e) => setDetailAddress(e.target.value)}
                                    className="border p-2 w-full"
                                    placeholder="상세주소"
                                />
                            </div>

                            <div className="mb-3">
                                <label className="block text-sm font-medium mb-1">수령인</label>
                                <input
                                    type="text"
                                    value={recipient}
                                    onChange={(e) => setRecipient(e.target.value)}
                                    className="border p-2 w-full"
                                    placeholder="예) 홍길동"
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label className="block text-sm font-medium mb-1">전화번호</label>
                                <input
                                    type="text"
                                    value={phone}
                                    onChange={(e) => setPhone(e.target.value)}
                                    className="border p-2 w-full"
                                    placeholder="예) 010-1234-5678"
                                    required
                                />
                            </div>
                            <div className="mb-3">
                                <label className="block text-sm font-medium mb-1">결제수단</label>
                                <div className="flex gap-4 mt-1">
                                    <label className="flex items-center gap-1">
                                        <input
                                            type="radio"
                                            name="paymentMethod"
                                            value="CARD"
                                            checked={paymentMethod === 'CARD'}
                                            onChange={(e) => setPaymentMethod(e.target.value)}
                                        />
                                        신용카드
                                    </label>
                                    <label className="flex items-center gap-1">
                                        <input
                                            type="radio"
                                            name="paymentMethod"
                                            value="TOSS"
                                            checked={paymentMethod === 'TOSS'}
                                            onChange={(e) => setPaymentMethod(e.target.value)}
                                        />
                                        토스
                                    </label>
                                </div>
                            </div>
                        </div>
                        {/* 결제 요약 */}
                        <div className="border border-gray-200 rounded p-4">
                            <h2 className="text-lg font-medium mb-4">결제 요약</h2>
                            <div className="flex justify-between mb-2">
                                <span>상품 금액</span>
                                <span>{subtotal.toLocaleString()}원</span>
                            </div>
                            <div className="flex justify-between mb-2">
                                <span>할인</span>
                                <span>-{discount.toLocaleString()}원</span>
                            </div>
                            <div className="flex justify-between mb-2">
                                <span>배송비</span>
                                <span>{shippingFee.toLocaleString()}원</span>
                            </div>
                            <hr className="my-3" />
                            <div className="flex justify-between mb-4 font-bold text-lg">
                                <span>총 결제 금액</span>
                                <span className="text-red-600">
                  {(paymentInfo.pricesSales + shippingFee).toLocaleString()}원
                </span>
                            </div>
                            <button
                                onClick={handlePayment}
                                className="w-full bg-black text-white py-3 rounded hover:bg-gray-800 transition-colors"
                            >
                                결제하기
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
