"use client";

import { useState, useEffect } from "react";
import { ReqQuestionDto } from "./types";
import { useRouter, useSearchParams } from "next/navigation";
import { PutMyQuestion } from "./api";
import Sidebar from "@/app/components/my/Sidebar";

export default function ModifyQuestion() {
    const searchParams = useSearchParams();
    const router = useRouter();
  
    // ✅ 상태 초기화
    const [id, setId] = useState<string | null>(null);
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [imageUrls, setImageUrls] = useState<string[]>([]);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState<string | null>(null);
  
    useEffect(() => {
        // ✅ sessionStorage에서 데이터 불러오기
        const storedData = sessionStorage.getItem("modifyQuestion");
        if (storedData) {
          const { id, title, content,imageUrls } = JSON.parse(storedData);
          setId(id);
          setTitle(title);
          setContent(content);
          setImageUrls(imageUrls);
        } else {
          console.error("수정할 데이터가 없습니다.");
          router.push("/my/question"); // 데이터 없을 경우 리스트 페이지로 이동
        }
      }, []);
    
    // ✅ 입력 값 변경 핸들러
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
      if (e.target.name === "title") setTitle(e.target.value);
      if (e.target.name === "content") setContent(e.target.value);
    };
  
    // ✅ 폼 제출 핸들러 (PUT 요청)
    const handleSubmit = async (e: React.FormEvent) => {
      e.preventDefault();
      setLoading(true);
      setMessage(null);
  
      if (!id) {
        setMessage("잘못된 요청입니다. ID가 존재하지 않습니다.");
        setLoading(false);
        return;
      }
  
      const numericId = Number(id);
      if (isNaN(numericId)) {
        setMessage("올바르지 않은 ID 값입니다.");
        setLoading(false);
        return;
      }
  
      try {
        const formData: ReqQuestionDto = { title, content };
        console.log(formData.title);
        console.log(formData.content);
        const response = await PutMyQuestion(formData, numericId);
        console.log("status code")
        console.log(response.status)
        if (!response.ok) throw new Error("서버 요청 실패!");
  
        setMessage("질문이 성공적으로 수정되었습니다!");
        setTitle("");
        setContent("");
        router.push("/my/question");
      } catch (error) {
        setMessage("질문 수정에 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };
  
    if (!id) return <p className="text-red-500 text-center">잘못된 요청입니다.</p>;
  
    return (
      <div className="flex">
        <Sidebar />
        <main className="flex-1 p-6 relative">
          <div className="container mx-auto p-4 max-w-lg">
            <h1 className="text-2xl font-bold mb-4">질문 수정</h1>
  
            {message && <p className="mb-4 text-center text-red-500">{message}</p>}

            <div className="mt-6">
              {imageUrls.length > 0 ? (
              <div className="grid grid-cols-3 gap-4 mt-4">
                   {imageUrls.map((url, index) => (
                       <img
                           key={index}
                           src={url}
                           alt={`Image ${index}`}
                           className="rounded-lg shadow-lg"
                      />
                   ))}
               </div>
              ) : (
              <p className="text-gray-500 mt-2">등록된 이미지가 없습니다.</p>
             )}
            </div>
  
            <form onSubmit={handleSubmit} className="space-y-4">
              {/* 제목 입력 */}
              <input
                type="text"
                name="title"
                placeholder="제목을 입력하세요"
                value={title}
                onChange={handleChange}
                required
                className="w-full border p-2 rounded-md"
              />
  
              {/* 내용 입력 */}
              <textarea
                name="content"
                placeholder="내용을 입력하세요"
                value={content}
                onChange={handleChange}
                required
                rows={4}
                className="w-full border p-2 rounded-md"
              />
  
              {/* 제출 버튼 */}
              <button
                type="submit"
                disabled={loading}
                className="w-full bg-blue-500 text-white p-2 rounded-md"
              >
                {loading ? "수정 중..." : "수정 완료"}
              </button>
            </form>
          </div>
        </main>
      </div>
    );
  }