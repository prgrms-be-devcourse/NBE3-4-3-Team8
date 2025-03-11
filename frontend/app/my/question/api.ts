

async function apiRequest(url: string, method: string, body?: object): Promise<Response> {
    return fetch(new URL(url, window.location.origin).toString(), {
        method,
        headers: { "Content-Type": "application/json" },
        body: body ? JSON.stringify(body) : undefined,
    });
}

export async function GetMyPage(page: number, pageSize: number = 10): Promise<Response> {
    console.log(`my/question/api.ts - GetQuestions (page: ${page}, pageSize: ${pageSize})`);
    return apiRequest(`/api/my/question?page=${page}&pageSize=${pageSize}`, "GET");
}

// 커서 기반 페이지네이션 방식
export async function GetMyPageCursor({
    before,
    after,
    pageSize = 10,
}: {
    before?: string;
    after?: string;
    pageSize?: number;
}): Promise<Response> {
    let apiUrl = `/api/my/question?pageSize=${pageSize}`;

    if (before) {
        apiUrl += `&before=${before}`;
        console.log(`Fetching older questions before: ${before}`);
    } else if (after) {
        apiUrl += `&after=${after}`;
        console.log(`Fetching newer questions after: ${after}`);
    }

    return apiRequest(apiUrl, "GET");
}


