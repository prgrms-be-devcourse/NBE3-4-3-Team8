"use client"

import { useEffect, useState } from "react";
import { GetMyPage } from "./api"; // API 함수 가져오기
import { PageDto, QuestionListDto } from "./types"; // DTO 타입 가져오기
import Sidebar from '@/app/components/my/Sidebar';
import { useRouter } from 'next/navigation';


export default function Home() {
    
    const router = useRouter();
    const [pageData, setPageData] = useState<PageDto<QuestionListDto> | null>(null);
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
  
          const data: PageDto<QuestionListDto> = await response.json();
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
      <div className="flex">
        <Sidebar />
        <main className="flex-1 p-6 relative"> 
          {/* ✅ 오른쪽 위 질문 등록 버튼 */}
          <button
            onClick={() => router.push('/my/question/createForm')}
            className="absolute top-6 right-6 bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition"
          >
            질문 등록
          </button>
  
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
                      <p className="text-sm text-gray-400">
                        생성일: {new Date(question.createDate).toLocaleString()}
                      </p>
                      {/* ✅ isAnswer이 true이면 "답변 완료" 표시 */}
                      {question.isAnswer && (
                      <p className="text-sm text-green-500 absolute bottom-2 right-4">
                      답변 완료
                      </p>
                      )}
                      {/* ✅ 이동 버튼 추가 */}
                      <button
                        className="mt-2 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition"
                        onClick={() => router.push(`/my/question/${question.id}`)}
                      >
                        상세 보기
                      </button>
                    </li>
                  ))}
                </ul>
  
                {/* ✅ 페이지네이션 버튼 (간격 조정) */}
                <div className="flex justify-center mt-6 gap-2">
                  <button
                    disabled={currentPage === 1}
                    onClick={() => setCurrentPage((prev) => prev - 1)}
                    className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md disabled:opacity-50"
                  >
                    이전
                  </button>
                  <span className="text-gray-700 flex items-center">
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
        </main>
      </div>
    );
  };
