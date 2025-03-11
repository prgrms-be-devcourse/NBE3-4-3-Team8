'use client';

import React, { useEffect, useState } from 'react';
import { useSearchParams, useRouter } from 'next/navigation';

export default function PaymentFailPage() {
  const searchParams = useSearchParams();
  const router = useRouter();
  const [errorInfo, setErrorInfo] = useState({
    code: '',
    message: '',
  });

  useEffect(() => {
    const code = searchParams.get('code') || '알 수 없는 오류';
    const message = searchParams.get('message') || '결제 처리 중 오류가 발생했습니다.';

    setErrorInfo({
      code,
      message,
    });
  }, [searchParams]);

  return (
      <div className="flex flex-col items-center justify-center min-h-screen p-4">
        <div className="w-full max-w-md p-6 bg-white rounded-lg shadow-md">
          <h2 className="text-2xl font-bold mb-6 text-center text-red-600">결제 실패</h2>

          <div className="space-y-4">
            <div className="p-3 bg-gray-50 rounded">
              <p className="font-medium">에러코드: {errorInfo.code}</p>
            </div>

            <div className="p-3 bg-gray-50 rounded">
              <p className="font-medium">실패 사유: {errorInfo.message}</p>
            </div>
          </div>

          <div className="mt-8 flex justify-center space-x-4">
            <button
                onClick={() => router.push('/')}
                className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
              홈으로
            </button>
            <button
                onClick={() => router.push('/order')}
                className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
            >
              결제 다시 시도
            </button>
          </div>
        </div>
      </div>
  );
}