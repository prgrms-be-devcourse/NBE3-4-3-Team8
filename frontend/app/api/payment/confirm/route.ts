// /app/api/payments/confirm/route.ts
import { NextResponse } from 'next/server';

export async function POST(request: Request) {
  try {
    const requestBody = await request.json();

    // 스프링부트 서버 URL
    const backendUrl = process.env.BACKEND_URL || 'http://localhost:8080';

    // 스프링부트 서버에 요청 전달
    const response = await fetch(`${backendUrl}/api/payments/confirm`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestBody),
    });

    const responseData = await response.json();

    // 스프링부트 서버의 응답을 그대로 클라이언트에 전달
    return NextResponse.json(responseData, {
      status: response.status,
    });
  } catch (error) {
    console.error('결제 승인 요청 중 오류:', error);
    return NextResponse.json(
        { message: '서버 오류가 발생했습니다.', code: 'SERVER_ERROR' },
        { status: 500 }
    );
  }
}