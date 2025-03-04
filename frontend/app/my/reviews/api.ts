
async function apiRequest(url: string, method: string, body?: object): Promise<Response> {
    return fetch(new URL(url, window.location.origin).toString(), {
        method,
        headers: { "Content-Type": "application/json" },
        body: body ? JSON.stringify(body) : undefined,
    });
}

export async function GetMyReviews(page: number): Promise<Response> {
    console.log(`my/reviews/api.ts - GetMyReviews (page: ${page})`);
    return apiRequest(`/api/my/reviews?page=${page}`, "GET");
}