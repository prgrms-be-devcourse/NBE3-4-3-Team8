'use client';

import { useEffect, useState,use  } from 'react';
import { useRouter, useParams, usePathname } from "next/navigation"; 
import { QuestionDto,QuestionGenFileDto } from "./types"; // ✅ 타입 정의 파일 import
import { useFetchImages } from "./api";
import { GetQuestion,DeleteQuestion } from "./api";


export default function Home()  {
    const params = useParams();
    const pathname = usePathname(); // ✅ 현재 경로 확인
    const [question, setQuestion] = useState<QuestionDto | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const router = useRouter();
  
    const id = params.id ? params.id.toString() : null; 
  
    useEffect(() => {
        if (!id || isNaN(Number(id))) return;

        if (pathname !== `/my/question/${id}`) {
            console.log("다른 페이지로 이동했으므로 API 호출을 중단합니다.");
            return;
        }

        let isMounted = true;

        const fetchQuestion = async () => {
            try {
                const response = await GetQuestion(Number(id));
                if (!response.ok) throw new Error("질문 데이터를 불러오는 데 실패했습니다.");
                const data: QuestionDto = await response.json();
                console.log("question data:")
                console.log(data)

                
                if (isMounted) {
                    setQuestion(data);
                }


            } catch (err) {
                setError(err instanceof Error ? err.message : "알 수 없는 오류 발생");
            } finally {
                if (isMounted) {
                    setLoading(false);
                }
            }
        };

        fetchQuestion();

        return () => {
            isMounted = false;
        };

       
    }, [id, pathname]); // ✅ `id` 값이 변경될 때 실행

    const imageUrls = useFetchImages(question!);
  
    const handleModify = () => {
      if (!id || isNaN(Number(id))) {
        console.error("ID 값이 올바르지 않습니다. 이동할 수 없습니다.");
        alert("ID 값이 존재하지 않습니다.");
        return;
      }
  
      // ✅ API 요청 중단 (이전 요청이 남아있을 경우)
      setQuestion(null);
      setLoading(false);
  
      // ✅ sessionStorage에 데이터 저장
      sessionStorage.setItem("modifyQuestion", JSON.stringify({
        id,
        title: question?.title || "",
        content: question?.content || "",
        imageUrls: imageUrls || null,
        
      }));
  
      router.push("/my/question/modifyForm");
    };

    const handleDelete = async () => {
        if (!id || isNaN(Number(id))) {
            console.error("ID 값이 올바르지 않습니다.");
            alert("삭제할 질문이 없습니다.");
            return;
        }

        if (!window.confirm("정말로 이 질문을 삭제하시겠습니까?")) return;

        try {
            const response = await DeleteQuestion(Number(id));
            if (!response.ok) throw new Error("질문 삭제에 실패했습니다.");

            alert("질문이 성공적으로 삭제되었습니다.");
            router.push("/my/question"); // ✅ 삭제 후 질문 목록으로 이동
        } catch (err) {
            console.error("Delete error:", err);
            alert("삭제 중 오류가 발생했습니다.");
        }
    };
  
  

  if (loading) return <p className="text-center text-gray-500">데이터를 불러오는 중...</p>;
  if (error) return <p className="text-center text-red-500">{error}</p>;
  if (!question) return <p className="text-center text-gray-500">질문을 찾을 수 없습니다.</p>;

  return (
    <div className="container mx-auto p-6 max-w-2xl">
      <button
        className="mb-4 px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition"
        onClick={() => router.push('/my/question')}
      >
        뒤로가기
      </button>

      

      <div className="p-6 border rounded-md shadow-md bg-white">
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
        <h1 className="text-2xl font-bold">{question.title}</h1>
        <p className="text-gray-700 mt-2">{question.content}</p>
        <p className="text-sm text-gray-500 mt-2">
          생성일: {new Date(question.createDate).toLocaleString()}
        </p>
        {question.isAnswer && <p className="text-sm text-green-500 font-semibold mt-2">답변 완료</p>}

        <div className="flex gap-4 mt-4">
          <button
            className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition"
            onClick={handleModify}
          >
            수정하기
          </button>

          <button
            className="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition"
            onClick={handleDelete}
          >
            삭제하기
          </button>
        </div>
      </div>

      

      <div className="mt-6">
        <h2 className="text-xl font-bold">답변 목록</h2>
        {question.answers && question.answers.length > 0 ? ( // ✅ undefined 체크 추가
          <ul className="mt-4 space-y-4">
            {question.answers.map((answer) => (
              <li key={answer.id} className="p-4 border rounded-md shadow-md bg-gray-100">
                <p>{answer.content}</p>
                <p className="text-sm text-gray-500">
                  작성일: {new Date(answer.createDate).toLocaleString()}
                </p>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-500 mt-2">아직 답변이 없습니다.</p>
        )}
      </div>
    </div>
  );
};
