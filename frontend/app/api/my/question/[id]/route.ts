import { NextRequest, NextResponse } from "next/server";

export async function GET(req: NextRequest, { params }: { params: { id: string } }) {
  const id = params.id; // ✅ await 제거

  console.log("-----------------------------------------------");
  console.log("GET Questions");
  console.log("question id : ", id);
  console.log("-----------------------------------------------");

  const cookies = req.headers.get("cookie") || "";

  const response = await fetch(`http://localhost:8080/my/question/${id}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
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

export async function DELETE(req: Request, { params }: { params: { id: number } })  {

    const { id } = params; // URL의 id 가져오기
    console.log("-----------------------------------------------");
    console.log("DELETE Question");
    console.log("-----------------------------------------------");
    const cookies = req.headers.get("cookie") || "";
       
    const response = await fetch(`http://localhost:8080/my/question/${id}`, {
        method: "DELETE",
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

export async function PUT(req: Request, { params }: { params: { id: number } }){
    const { id } = params;
    console.log("-----------------------------------------------");
    console.log(`api/my/route.ts - PUT QUESTION `);
    console.log("-----------------------------------------------");
    const cookies = req.headers.get("cookie") || "";
    try {
      const requestBody = await req.json(); // 🔹 요청 데이터 가져오기
      console.log("Received request body:", requestBody);
  
      const response = await fetch(`http://localhost:8080/my/question/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" ,
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
      console.error("Error processing PUT request:", error);
      return new Response(JSON.stringify({ error: "Failed to process request" }), {
        status: 500,
        headers: { "Content-Type": "application/json" },
    });
    }
  };