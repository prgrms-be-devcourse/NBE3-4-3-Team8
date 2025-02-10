import { NextResponse } from "next/server";


// 🔹 클라이언트 요청에서 JWT 토큰 추출 함수
function getJwtTokenFromHeaders(req: Request): string | null {
    const authHeader = req.headers.get("Authorization");
    if (authHeader && authHeader.startsWith("Bearer ")) {
        return authHeader.split("Bearer ")[1]; // 🔹 "Bearer " 다음 토큰 부분만 추출
    }
    return null;
}

export async function GET(req: Request)  {
    
    console.log("-----------------------------------------------");
    console.log("GET MyPage");
    console.log("-----------------------------------------------");
    
    
    const jwtToken = getJwtTokenFromHeaders(req); // 🔹 클라이언트에서 보낸 JWT 토큰 추출

    if (!jwtToken) {
        console.log("JWT Token not found in Authorization header");
        return new Response(JSON.stringify({ error: "Unauthorized" }), {
            status: 401,
            headers: { "Content-Type": "application/json" },
        });
    }
    

    const response = await fetch(`http://localhost:8080/api/auth/my`, {
        method: "GET",
        headers: { "Content-Type": "application/json",
                   "Authorization": `Bearer ${jwtToken}`,
         },
        redirect: "manual"
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

    const jwtToken = getJwtTokenFromHeaders(req); // 🔹 클라이언트에서 보낸 JWT 토큰 추출

    if (!jwtToken) {
        console.log("JWT Token not found in Authorization header");
        return new Response(JSON.stringify({ error: "Unauthorized" }), {
            status: 401,
            headers: { "Content-Type": "application/json" },
        });
    }

    try {
        
        const requestBody = await req.json(); // 🔹 요청의 body 데이터 가져오기
        console.log("Received request body:", requestBody);

        const response = await fetch(`http://localhost:8080/api/auth/my`, {
            method: "PUT",
            headers: { "Content-Type": "application/json",
                       "Authorization": `Bearer ${jwtToken}`,
             },
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


