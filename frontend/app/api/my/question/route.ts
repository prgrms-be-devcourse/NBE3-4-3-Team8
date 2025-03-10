import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest)  {

  const { searchParams } = new URL(req.url);
  const page = parseInt(searchParams.get("page") || "0", 10); // 기본값 1
    
  console.log("-----------------------------------------------");
  console.log("GET Questions");
  console.log("-----------------------------------------------");
  const cookies = req.headers.get("cookie") || "";
     
  const response = await fetch(`http://localhost:8080/my/question?page=${page}`, {
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

export const POST = async (req: Request) => {
    console.log("-----------------------------------------------");
    console.log(`api/my/route.ts - Post DeliveryInformation `);
    console.log("-----------------------------------------------");
    const cookies = req.headers.get("cookie") || "";
    try {
      const requestBody = await req.json(); // 🔹 요청 데이터 가져오기
      console.log("Received request body:", requestBody);
  
      const response = await fetch(`http://localhost:8080/my/question`, {
        method: "POST",
        headers: { "Content-Type": "application/json",
                    cookie: cookies,
         },
        body: JSON.stringify(requestBody),
      });
  
      console.log("Backend response status:", response.status);
      console.log("Backend response headers:", response.headers);
      

        // 🔹 백엔드 응답을 그대로 반환
        return new Response(response.body, {
            status: response.status,
            headers: response.headers,
        });
    } catch (error) {
      console.error("Error processing POST request:", error);
      return new Response(JSON.stringify({ error: "Failed to process request" }), {
        status: 500,
        headers: { "Content-Type": "application/json" },
    });
    }
  };