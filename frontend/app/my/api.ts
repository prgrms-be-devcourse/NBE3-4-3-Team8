
import { MemberMyPageDto, DeliveryInformationDto } from "./types";

function getJwtTokenFromCookie(): string | null {
    const cookieHeader = document.cookie;
    const jwtToken = cookieHeader
        .split("; ")
        .find(row => row.startsWith("jwtToken="))
        ?.split("=")[1];

    return jwtToken || null;
}

// 공통 요청 함수
async function apiRequest(url: string, method: string, body?: object): Promise<Response> {
    const jwtToken = getJwtTokenFromCookie();

    const headers: HeadersInit = {
        "Content-Type": "application/json",
    };

    if (jwtToken) {
        headers["Authorization"] = `Bearer ${jwtToken}`; // 🔹 JWT 추가
    }

    return fetch(new URL(url, window.location.origin).toString(), {
        method,
        headers,
        body: body ? JSON.stringify(body) : undefined,
    });
}

//마이페이지 정보 가져오기
export async function GetMyPage(): Promise<Response> {
    console.log("my-page/api.ts - GetMyPage");
    return apiRequest("/api/my", "GET");
}

//마이페이지 정보 수정
export async function PutMyPage(updatedData: MemberMyPageDto): Promise<Response> {
    console.log("my-page/api.ts - PutMyPage");
    return apiRequest("/api/my", "PUT", updatedData);
}

//특정 배송지 정보 수정
export async function PutMyAddress(deliveryInformation: DeliveryInformationDto, id: number): Promise<Response> {
    console.log("my-page/api.ts - PutMyAddress");
    return apiRequest(`/api/my/deliveryInformation/${id}`, "PUT", deliveryInformation);
}

//새 배송지 추가
export async function PostAddress(deliveryInformation: DeliveryInformationDto): Promise<Response> {
    console.log("my-page/api.ts - PostAddress");
    return apiRequest("/api/my/deliveryInformation", "POST", deliveryInformation);
}

// 특정 배송지 삭제
export async function DeleteAddress(id: number): Promise<Response> {
    console.log("my-page/api.ts - DeleteAddress");
    return apiRequest(`/api/my/deliveryInformation/${id}`, "DELETE");
}