import { NextResponse } from "next/server";

export async function DELETE(req: Request, { params }: { params: { id: number } })  {

    const { id } = params; // URL의 id 가져오기
    console.log("-----------------------------------------------");
    console.log("DELETE DeliveryInformation");
    console.log("-----------------------------------------------");
    
       
    const response = await fetch(`http://localhost:8080/my/deliveryInformation/${id}`, {
        method: "DELETE",
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

export async function PUT(req: Request, { params }: { params: { id: number } }){
    const { id } = params;
    console.log("-----------------------------------------------");
    console.log(`api/my/route.ts - PUT DeliveryInformation `);
    console.log("-----------------------------------------------");
  
    try {
      const requestBody = await req.json(); // 🔹 요청 데이터 가져오기
      console.log("Received request body:", requestBody);
  
      const response = await fetch(`http://localhost:8080/my/deliveryInformation/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
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