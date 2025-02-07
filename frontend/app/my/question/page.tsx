"use client"

import { useEffect, useState } from "react";
import { GetMyPage } from "./api"; // API 함수 가져오기
import { PageDto, QuestionDto } from "./types"; // DTO 타입 가져오기


export default function Home() {

    const [pageData, setPageData] = useState<PageDto<QuestionDto> | null>(null);
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지 번호
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
  
    // 🔹 질문 목록 데이터 가져오기
    useEffect(() => {
      async function fetchData() {
        setLoading(true);
        setError(null);
  
        try {
          const response = await GetMyPage(currentPage);
          if (!response.ok) throw new Error("데이터를 불러오는 데 실패했습니다.");
  
          const data: PageDto<QuestionDto> = await response.json();
          setPageData(data);
        } catch (error) {
          setError(error instanceof Error ? error.message : "알 수 없는 오류 발생");
        } finally {
          setLoading(false);
        }
      }
  
      fetchData();
    }, [currentPage]);
  
    return (
      <div className="container mx-auto p-4 max-w-2xl">
        <h1 className="text-2xl font-bold mb-4">질문 목록</h1>
  
        {loading && <p className="text-center text-gray-500">데이터를 불러오는 중...</p>}
        {error && <p className="text-center text-red-500">{error}</p>}
  
        {/* 질문 목록 렌더링 */}
        {pageData && (
          <>
            <ul className="space-y-4">
              {pageData.items.map((question) => (
                <li key={question.id} className="p-4 border rounded-md shadow">
                  <h2 className="text-lg font-semibold">{question.title}</h2>
                  <p className="text-gray-600">{question.content}</p>
                  <p className="text-sm text-gray-400">
                    생성일: {new Date(question.createDate).toLocaleString()}
                  </p>
                </li>
              ))}
            </ul>
  
            {/* 페이지네이션 버튼 */}
            <div className="flex justify-between mt-6">
              <button
                disabled={currentPage === 1}
                onClick={() => setCurrentPage((prev) => prev - 1)}
                className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md disabled:opacity-50"
              >
                이전
              </button>
              <span className="text-gray-700">
                {pageData.currentPageNumber} / {pageData.totalPages}
              </span>
              <button
                disabled={currentPage >= pageData.totalPages}
                onClick={() => setCurrentPage((prev) => prev + 1)}
                className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md disabled:opacity-50"
              >
                다음
              </button>
            </div>
          </>
        )}
      </div>
    );
}