import { useState, useEffect } from "react";
import { QuestionDto, QuestionGenFileDto } from "./types";

const API_BASE_URL = "http://localhost:8080"; // API 주소 변경 필요

export function useFetchImages(question: QuestionDto) {
    const [imageUrls, setImageUrls] = useState<string[]>([]);
    console.log("questionFile:")
    console.log(question)
    useEffect(() => {
      
      if (!question || !question.genFiles) return;
      
      const fetchImages = async () => {
        const urls = await Promise.all(
          question.genFiles.map(async (file: QuestionGenFileDto) => {
            console.log(`fetch: ${API_BASE_URL}/my/question/genFile/download/${question.id}/${file.fileName}`)
            const response = await fetch(
              `${API_BASE_URL}/my/question/genFile/download/${question.id}/${file.fileName}`, {
                method: 'GET',
                credentials: 'include',
              }
            );

            
  
            if (!response.ok) {
              console.error(`Failed to load image: ${file.fileName}`);
              return null;
            }
  
            const blob = await response.blob();
            return URL.createObjectURL(blob);
          })
        );
  
        setImageUrls(urls.filter(Boolean) as string[]);
      };
  
      fetchImages();
    }, [question]);
    console.log("urls");
    console.log(imageUrls);
    return imageUrls;
  }

  async function apiRequest(url: string, method: string, body?: object): Promise<Response> {
      return fetch(new URL(url, window.location.origin).toString(), {
          method,
          headers: { "Content-Type": "application/json" },
          body: body ? JSON.stringify(body) : undefined,
      });
  }
  
  export async function GetQuestion(id: number): Promise<Response> {
      console.log(`my/question/{id} api.ts - GetQuestion (id: ${id})`);
      return apiRequest(`/api/my/question/${id}`, "GET");
  }
  
  
  // 특정 배송지 삭제
  export async function DeleteQuestion(id: number): Promise<Response> {
      console.log("my-page/api.ts - DeleteAddress");
      return apiRequest(`/api/my/question/${id}`, "DELETE");
  }
