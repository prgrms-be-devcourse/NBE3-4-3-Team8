import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest)  {

  const { searchParams } = new URL(req.url);
  const cookies = req.headers.get("cookie") || "";
  
  const page = searchParams.get("page");
  const lastQuestionId = searchParams.get("lastQuestionId");
  const firstQuestionId = searchParams.get("firstQuestionId");
  
  let apiUrl = "http://localhost:8080/my/question";

  if (page !== null) {
    apiUrl += `?page=${page}`;
  } else if (lastQuestionId !== null) {
    apiUrl += `?lastQuestionId=${lastQuestionId}`;
  } else if (firstQuestionId !== null) {
    apiUrl += `?firstQuestionId=${firstQuestionId}`;
  }

  console.log("-----------------------------------------------");
  console.log("GET Questions");
  console.log("Requested URL:", apiUrl);
  console.log("-----------------------------------------------");

     
  const response = await fetch(apiUrl, {
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