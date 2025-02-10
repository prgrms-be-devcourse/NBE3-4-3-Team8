import {  ReqQuestionDto } from "./types";

async function apiRequest(url: string, method: string, body?: object): Promise<Response> {
    return fetch(new URL(url, window.location.origin).toString(), {
        method,
        headers: { "Content-Type": "application/json" },
        body: body ? JSON.stringify(body) : undefined,
    });
}

export async function PostQuestion(reqQuestionDto: ReqQuestionDto): Promise<Response> {
    console.log("my/question /api.ts - PostQuestion");
    return apiRequest("/api/my/question", "POST", reqQuestionDto);
}