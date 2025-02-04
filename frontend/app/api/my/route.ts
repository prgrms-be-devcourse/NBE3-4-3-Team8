import { NextResponse } from "next/server";

export async function GET()  {
    
    console.log("-----------------------------------------------");
    console.log("GET MyPage");
    console.log("-----------------------------------------------");
    
       
    const response = await fetch(`http://localhost:8080/my`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
    });

    console.log("Backend response status:", response.status);
    console.log("Backend response headers:", response.headers);
      

    // 🔹 백엔드 응답을 그대로 반환
    return new Response(response.body, {
        status: response.status,
        headers: response.headers,
    });
    
}

export async function PUT(req: Request) {
    

    console.log("-----------------------------------------------");
    console.log(`api/my/route.ts - Put MyPage `);
    console.log("-----------------------------------------------");

    try {
        const requestBody = await req.json(); // 🔹 요청의 body 데이터 가져오기
        console.log("Received request body:", requestBody);

        const response = await fetch(`http://localhost:8080/my`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestBody), // 🔹 받은 body 데이터를 그대로 백엔드로 전달
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
}


