import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest)  {

  const { searchParams } = new URL(req.url);
  const page = parseInt(searchParams.get("page") || "0", 10); // 기본값 1
    
  console.log("-----------------------------------------------");
  console.log("GET reviews");
  console.log("-----------------------------------------------");
  const cookies = req.headers.get("cookie") || "";
  console.log(cookies)
     
  const response = await fetch(`http://localhost:8080/api/auth/me/my/reviews?page=${page}`, {
      method: "GET",
      headers: { "Content-Type": "application/json",
                 cookie: cookies,
       },
  });

  console.log("Backend response status:", response.status);
  console.log("Backend response headers:", response.headers);
    

  // 🔹 백엔드 응답을 그대로 반환
  return new Response(response.body, {
      status: response.status,
      headers: response.headers,
  });
  
}