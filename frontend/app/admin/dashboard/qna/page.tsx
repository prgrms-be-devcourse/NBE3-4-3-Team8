'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';

interface Question {
  id: number;
  title: string;
  memberEmail: string;
  createDate: string;
  hasAnswer: boolean;
}

export default function QuestionListPage() {
  const router = useRouter();
  const [questions, setQuestions] = useState<Question[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [keyword, setKeyword] = useState('');
  const [answerFilter, setAnswerFilter] = useState<'all' | 'answered' | 'unanswered'>('all');
  const [loading, setLoading] = useState(false);

  // 절대경로 BASE_URL 설정
  const BASE_URL = 'http://localhost:8080/admin/dashboard';

  useEffect(() => {
    fetchQuestions();
  }, [page, keyword, answerFilter]);

  const fetchQuestions = async () => {
    setLoading(true);
    try {
      // URL 객체 사용하여 쿼리 파라미터 안전하게 추가
      const url = new URL(`${BASE_URL}/questions`);
      url.searchParams.append('page', page.toString());
      if (keyword) url.searchParams.append('keyword', keyword);
      if (answerFilter !== 'all') {
        // API에 전달할 값은 "true" 또는 "false" 문자열로 설정
        url.searchParams.append('hasAnswer', answerFilter === 'answered' ? 'true' : 'false');
      }

      console.log('🔄 API 요청 URL:', url.toString());

      const { data } = await axios.get(url.toString(), { withCredentials: true });
      console.log('✅ 질문 목록 응답 데이터:', data);

      // 응답 데이터 구조에 맞춰 상태 업데이트 (items, totalPages)
      setQuestions(data?.items ?? []);
      setTotalPages(data?.totalPages ?? 1);
      console.log('📌 업데이트된 questions 상태:', data?.items);
    } catch (error: any) {
      console.error('❌ 질문 목록 조회 실패:', error.response?.data ?? error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-9xl mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">질문 목록</h1>

      {/* 검색 및 필터 영역 */}
      <div className="flex flex-col sm:flex-row gap-4 mb-6">
        <input
          type="text"
          placeholder="제목 또는 작성자 검색"
          value={keyword}
          onChange={(e) => {
            setKeyword(e.target.value);
            setPage(0); // 검색 시 페이지 초기화
          }}
          className="flex-1 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <select
          value={answerFilter}
          onChange={(e) => {
            setAnswerFilter(e.target.value as 'all' | 'answered' | 'unanswered');
            setPage(0);
          }}
          className="p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="all">전체</option>
          <option value="answered">답변 완료</option>
          <option value="unanswered">미답변</option>
        </select>
      </div>

      {/* 테이블 영역 */}
      <div className="overflow-x-auto">
        <table className="min-w-full bg-white shadow rounded-lg">
          <thead className="bg-blue-50">
            <tr>
              <th className="py-3 px-4 text-left text-sm font-semibold text-gray-700">번호</th>
              <th className="py-3 px-4 text-left text-sm font-semibold text-gray-700">제목</th>
              <th className="py-3 px-4 text-left text-sm font-semibold text-gray-700">작성자</th>
              <th className="py-3 px-4 text-left text-sm font-semibold text-gray-700">작성일</th>
              <th className="py-3 px-4 text-right text-sm font-semibold text-gray-700">
                답변 상태
              </th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr>
                <td colSpan={5} className="py-4 text-center text-gray-500">
                  로딩 중...
                </td>
              </tr>
            ) : questions.length > 0 ? (
              questions.map((q) => (
                <tr
                  key={q.id}
                  className={`border-b hover:bg-gray-100 cursor-pointer ${!q.hasAnswer ? 'bg-yellow-50' : ''}`}
                  onClick={() => router.push(`/admin/dashboard/qna/${q.id}`)}
                >
                  <td className="py-3 px-4 text-sm text-gray-600">{q.id}</td>
                  <td className="py-3 px-4 text-sm text-gray-800">{q.title}</td>
                  <td className="py-3 px-4 text-sm text-gray-600">{q.memberEmail}</td>
                  <td className="py-3 px-4 text-sm text-gray-600">
                    {new Date(q.createDate).toLocaleString()}
                  </td>
                  <td className="py-3 px-4 text-sm text-right">
                    {q.hasAnswer ? (
                      <span className="inline-block px-3 py-1 rounded-full bg-green-100 text-green-800 font-medium">
                        답변 완료
                      </span>
                    ) : (
                      <span className="inline-block px-3 py-1 rounded-full bg-red-100 text-red-800 font-medium">
                        미답변
                      </span>
                    )}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={5} className="py-4 text-center text-gray-500">
                  등록된 질문이 없습니다.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* 페이지네이션 영역 */}
      <div className="flex justify-between mt-6">
        <button
          disabled={page === 0}
          onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
          className="px-4 py-2 bg-gray-500 text-white rounded disabled:opacity-50"
        >
          이전
        </button>
        <span className="text-sm text-gray-700">
          {page + 1} / {totalPages}
        </span>
        <button
          disabled={page + 1 >= totalPages}
          onClick={() => setPage((prev) => prev + 1)}
          className="px-4 py-2 bg-gray-500 text-white rounded disabled:opacity-50"
        >
          다음
        </button>
      </div>
    </div>
  );
}
