"use client";
import { useEffect, useState } from "react";
import { GetMyPage, GetMyPageCursor } from "./api"; // API 함수 가져오기
import { PageDto, QuestionListDto, CursorPageDto } from "./types";
import Sidebar from '@/app/components/my/Sidebar';
import { useRouter } from 'next/navigation';

export default function Home() {
    const router = useRouter();
    const [pageData, setPageData] = useState<PageDto<QuestionListDto> | null>(null);
    const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 번호

    const [lastCursor, setLastCursor] = useState<string | null>(null);
    const [FirstCursor, setFirstCursor] = useState<string | null>(null);
    const [questions, setQuestions] = useState<QuestionListDto[]>([]);

    const [isCursorPaging, setIsCursorPaging] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const pageSize = 10; // 한 번에 보여줄 페이지 개수

    useEffect(() => {
        fetchPageData(1);
    }, []);

    const fetchPageData = async (page: number) => {
        setLoading(true);
        setError(null);
        if(page >=100) {
            alert("페이징 불가능!")
            return
        }
        try {
            const adjustedPageSize = page === 97 ? 30 : page === 98 ? 20 : 10;
            const response = await GetMyPage(page - 1, adjustedPageSize);
            if (!response.ok) throw new Error("데이터를 불러오는 데 실패했습니다.");
            const data: PageDto<QuestionListDto> = await response.json();
            setPageData({ ...data, items: data.items.slice(0, 10) });
            setCurrentPage(page);
            setLastCursor(data.items[data.items.length - 1]?.createDate || null);
        } catch (error) {
            setError(error instanceof Error ? error.message : "알 수 없는 오류 발생");
        } finally {
            setLoading(false);
        }
    };

    async function fetchCursorPage({ before, after }: { before?: string; after?: string }) {
        console.log("cursorfETCH")
        console.log("after: "+after)
        console.log("before: " + before)
        try {
            const response = await GetMyPageCursor({ before, after, pageSize: 10 });
        const data: CursorPageDto<QuestionListDto> = await response.json();
        console.log("resData")
        console.log(data)
        setQuestions(data.items);
        setLastCursor(data.nextCursor);  // 다음 페이지(오래된 데이터)
        setFirstCursor(data.prevCursor);

        // pageData를 완전히 새로 설정
        setPageData({
            items: data.items,  // 기존 items는 제거하고 새로 받은 items로 갱신
            currentPageNumber: 1,  // 커서 기반 페이지네이션에서는 1페이지로 시작
            pageSize: 10,  // pageSize는 항상 10으로 고정
            totalPages: Math.ceil(data.items.length / 10),  // totalPages 계산
            totalItems: data.items.length,  // 전체 항목 수를 data.items.length로 설정
        });
        } catch (error) {
            console.error("Error fetching cursor page:", error);
        }
    }
    

    const cursorPaginationButtons=() => {
        
        if (!pageData) return null;

        const totalPages = pageData.totalPages;

        // 페이지 번호 계산
        const startPage = Math.floor((currentPage - 1) / pageSize) * pageSize + 1;
        const endPage = Math.min(startPage + pageSize - 1, totalPages);

        const pageNumbers = [];
        for (let i = startPage; i <= endPage; i++) {
            pageNumbers.push(
                <button
                    key={i}
                    onClick={() => fetchPageData(i)}
                    className={`px-3 py-1 rounded-md transition ${currentPage === i ? "bg-blue-500 text-white font-bold" : "bg-gray-200 text-gray-700"}`}
                >
                    {i}
             </button>
            );
        }

        return(
        <div className="flex justify-center mt-6 gap-2">
             {currentPage < 100 && !isCursorPaging ? (
                <>
                    <button
                        disabled={currentPage === 1}
                        onClick={() => fetchPageData(1)}
                        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md disabled:opacity-50"
                    >
                        처음
                    </button>
                    <button
                        disabled={currentPage === 1}
                        onClick={() => fetchPageData(Math.max(1, currentPage - pageSize))}
                        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md disabled:opacity-50"
                    >
                        이전
                    </button>

                    {pageNumbers}

                    <button
                        disabled={currentPage + pageSize > totalPages}
                        onClick={() => fetchPageData(Math.min(totalPages, currentPage + pageSize))}
                        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md disabled:opacity-50"
                    >
                        다음
                    </button>
                    <button
                        disabled={currentPage === totalPages}
                        onClick={() => fetchPageData(totalPages)}
                        className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md disabled:opacity-50"
                    >
                        끝
                    </button>

                    {currentPage >= 97 && currentPage <= 99 && lastCursor && (
                        <button
                            onClick={() => {
                                setIsCursorPaging(true);
                                fetchCursorPage({ before: lastCursor });
                            }}
                            className="bg-yellow-500 text-white px-4 py-2 rounded-md"
                        >
                            날짜로 조회
                        </button>
                    )}
                </>
            ) : (
                    <>
                <button
                    disabled={!FirstCursor}
                    onClick={() => fetchCursorPage({ after: FirstCursor ? FirstCursor : undefined })}
                    className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md disabled:opacity-50"
                >
                    뒤로 이동
                </button>
                <button
                    className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md disabled:opacity-50"
                    >
                    {lastCursor?.slice(0, 19)}
                </button>
                <button
                    disabled={!lastCursor}
                    onClick={() => fetchCursorPage({ before: lastCursor ? lastCursor : undefined })}
                    className="bg-gray-300 text-gray-700 px-4 py-2 rounded-md disabled:opacity-50"
                >
                    앞으로 이동
                </button>
            </>
            )}
        </div>
        );
    }


    
    return (
        <div className="flex">
            <Sidebar />
            <main className="flex-1 p-6 relative"> 
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

                    {pageData && (
                        <>
                            <ul className="space-y-4">
                                {pageData.items.map((question) => (
                                    <li key={question.id} className="p-4 border rounded-md shadow">
                                        <h2 className="text-lg font-semibold">{question.title}</h2>
                                        <p className="text-sm text-gray-400">생성일: {new Date(question.createDate).toLocaleString()}</p>
                                        {question.isAnswer && (
                                            <p className="text-sm text-green-500 absolute bottom-2 right-4">답변 완료</p>
                                        )}
                                        <button
                                            className="mt-2 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition"
                                            onClick={() => router.push(`/my/question/${question.id}`)}
                                        >
                                            상세 보기
                                        </button>
                                    </li>
                                ))}
                            </ul>
                            {cursorPaginationButtons()}
                        </>
                    )}
                </div>
            </main>
        </div>
    );
}
