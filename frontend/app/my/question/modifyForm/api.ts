import {  ReqQuestionDto } from "./types";

async function apiRequest(url: string, method: string, body?: object): Promise<Response> {
    const fullUrl = window.location.origin + url; // ✅ URL 절대 경로 문제 해결

    console.log(`🔹 API 요청: ${fullUrl}, Method: ${method}, Body:`, body);

    try {
        const response = await fetch(fullUrl, {
            method,
            headers: { "Content-Type": "application/json" },
            body: body ? JSON.stringify(body) : undefined,
        });

        if (!response.ok) {
            console.error(`❌ API 요청 실패: ${response.status} ${response.statusText}`);
        }

        return response;
    } catch (error) {
        console.error(`❌ fetch 요청 중 오류 발생:`, error);
        throw error; // 오류를 다시 던짐
    }
}

//마이페이지 정보 수정
export async function PutMyQuestion(reqQuestionDto: ReqQuestionDto, id: number): Promise<Response> {
    console.log("my-question/api.ts - PutMyPage");
    return apiRequest(`/api/my/question/${id}`, "PUT", reqQuestionDto);
    
}

const API_BASE_URL = "http://localhost:8080";

export async function deleteImageFromServer(questionId: string, fileNo: number, typeCode: string): Promise<void> {
  try {
    const response = await fetch(`${API_BASE_URL}/my/question/genFile/${questionId}/${fileNo}/${typeCode}`, {
      method: "DELETE",
      credentials: 'include',
    });

    if (!response.ok) throw new Error("이미지 삭제 실패");
    console.log(`이미지 삭제 성공: ${fileNo}`);
  } catch (error) {
    console.error("이미지 삭제 오류:", error);
    throw error; // 에러를 다시 던져서 호출하는 쪽에서 처리할 수 있게 함
  }
}
