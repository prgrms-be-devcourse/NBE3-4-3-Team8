

async function apiRequest(url: string, method: string, body?: object): Promise<Response> {
    return fetch(new URL(url, window.location.origin).toString(), {
        method,
        headers: { "Content-Type": "application/json" },
        body: body ? JSON.stringify(body) : undefined,
    });
}

export async function GetMyPage(page: number): Promise<Response> {
    console.log(`my/question/api.ts - GetQuestions (page: ${page})`);
    return apiRequest(`/api/my/question?page=${page}`, "GET");
}