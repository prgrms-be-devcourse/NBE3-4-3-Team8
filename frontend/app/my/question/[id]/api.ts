import { useState, useEffect } from "react";
import { QuestionDto, QuestionGenFileDto, ImageUrlDto } from "./types";

const API_BASE_URL = "http://localhost:8080"; // API 주소 변경 필요

export function useFetchImages(question: QuestionDto | null) : ImageUrlDto[] {
    const [imageUrlDtos, setImageUrlDtos] = useState<ImageUrlDto[]>([]);
    console.log("questionFile:")
    console.log(question)
    useEffect(() => {
      
      if (!question || !question.genFiles) return;
      
      const fetchImages = async () => {
        const dtos = await Promise.all(
          question.genFiles.map(async (file: QuestionGenFileDto) => {
            console.log(`fetch: ${API_BASE_URL}/my/question/genFile/download/${question.id}/${file.fileNo}`)
            const response = await fetch(
              `${API_BASE_URL}/my/question/genFile/download/${question.id}/${file.fileNo}`, {
                method: 'GET',
                credentials: 'include',
              }
            );

            
  
            if (!response.ok) {
              console.error(`Failed to load image: ${file.fileNo}`);
              return null;
            }
  
            const blob = await response.blob();
            const imageUrl = URL.createObjectURL(blob);

            return {
              imageUrl,
              typeCode: file.typeCode,
              fileNo: file.fileNo
            };
          })
        );
  
        setImageUrlDtos(dtos.filter(Boolean) as ImageUrlDto[]);
      };
  
      fetchImages();
    }, [question]);
    console.log("urls");
    console.log(imageUrlDtos);
    return imageUrlDtos;
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
