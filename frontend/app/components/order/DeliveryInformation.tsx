import React, { useState, useEffect } from 'react';
import { DeliveryInformationDto } from '@/app/my/types';

interface DeliveryInformationProps {
  postCode: string;
  roadAddress: string;
  detailAddress: string;
  recipient: string;
  phone: string;
  onSearchAddress: () => void;
  setPostCode: (value: string) => void;
  setRoadAddress: (value: string) => void;
  setDetailAddress: (value: string) => void;
  setRecipient: (value: string) => void;
  setPhone: (value: string) => void;
}

export default function DeliveryInformation({
  postCode,
  roadAddress,
  detailAddress,
  recipient,
  phone,
  onSearchAddress,
  setPostCode,
  setRoadAddress,
  setDetailAddress,
  setRecipient,
  setPhone,
}: DeliveryInformationProps) {
  return (
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
            onClick={onSearchAddress}
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
          placeholder="수령인 이름"
          required
        />
      </div>
      <div className="mb-3">
        <label className="block text-sm font-medium mb-1">연락처</label>
        <input
          type="text"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
          className="border p-2 w-full"
          placeholder="연락처 (- 없이 입력)"
          required
        />
      </div>
    </div>
  );
}
