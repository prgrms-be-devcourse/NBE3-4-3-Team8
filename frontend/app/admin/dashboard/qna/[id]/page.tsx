'use client';

import { useParams, useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import axios from 'axios';
import Spinner from '@/app/components/admin/Spinner';
import ConfirmationModal from '@/app/components/admin/ConfirmationModal';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface Question {
  id: number;
  title: string;
  content: string;
  memberEmail: string;
  createDate: string;
  hasAnswer: boolean;
  answer?: Answer;
}

interface Answer {
  id: number;
  content: string;
  createDate: string;
  modifyDate: string;
}

export default function QuestionDetailPage() {
  const router = useRouter();
  const { id: questionId } = useParams();
  const [question, setQuestion] = useState<Question | null>(null);
  const [answerContent, setAnswerContent] = useState('');
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [confirmAction, setConfirmAction] = useState<() => void>(() => {});

  const BASE_URL = 'http://localhost:8080/admin/dashboard';

  const fetchQuestion = async () => {
    if (!questionId) return;
    setLoading(true);
    try {
      const url = `${BASE_URL}/questions/${questionId}`;
      const { data } = await axios.get(url, { withCredentials: true });
      setQuestion(data);
      setAnswerContent(data.answer?.content || '');
    } catch (error: any) {
      toast.error(`질문 조회 중 오류 발생: ${error.response?.data?.message || error.message}`);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchQuestion();
  }, [questionId]);

  // 질문 삭제 핸들러
  const handleQuestionDelete = async () => {
    try {
      const url = `${BASE_URL}/questions/${questionId}`;
      await axios.delete(url, { withCredentials: true });
      toast.success('질문이 삭제되었습니다.');
      router.push('/admin/dashboard/qna');
    } catch (error: any) {
      toast.error(`질문 삭제 실패: ${error.response?.data?.message || error.message}`);
    }
  };

  const confirmDeleteQuestion = () => {
    setConfirmAction(() => handleQuestionDelete);
    setShowConfirmModal(true);
  };

  // 답변 등록/수정 핸들러
  const handleAnswerSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!answerContent.trim()) {
      toast.warn('답변 내용을 입력해주세요.');
      return;
    }
    try {
      const url = question?.answer
        ? `${BASE_URL}/questions/${questionId}/answers/${question.answer.id}`
        : `${BASE_URL}/questions/${questionId}/answers`;
      const method = question?.answer ? 'put' : 'post';
      await axios[method](url, { content: answerContent }, { withCredentials: true });
      toast.success(question?.answer ? '답변이 수정되었습니다.' : '답변이 등록되었습니다.');
      fetchQuestion();
      setIsEditing(false);
    } catch (error: any) {
      toast.error(`답변 저장 실패: ${error.response?.data?.message || error.message}`);
    }
  };

  // 답변 삭제 핸들러
  const handleAnswerDelete = async () => {
    if (!question?.answer) return;
    try {
      const url = `${BASE_URL}/questions/${questionId}/answers/${question.answer.id}`;
      await axios.delete(url, { withCredentials: true });
      toast.success('답변이 삭제되었습니다.');
      setAnswerContent('');
      fetchQuestion();
    } catch (error: any) {
      toast.error(`답변 삭제 실패: ${error.response?.data?.message || error.message}`);
    }
  };

  const confirmDeleteAnswer = () => {
    setConfirmAction(() => handleAnswerDelete);
    setShowConfirmModal(true);
  };

  if (loading || !question) return <Spinner />;

  return (
    <div className="max-w-5xl mx-auto p-6">
      {showConfirmModal && (
        <ConfirmationModal
          title="확인"
          message="정말로 삭제하시겠습니까?"
          onConfirm={() => {
            confirmAction();
            setShowConfirmModal(false);
          }}
          onCancel={() => setShowConfirmModal(false)}
        />
      )}

      <div className="bg-white rounded-lg shadow p-6 mb-6">
        <div className="flex justify-between items-start mb-4">
          <h1 className="text-3xl font-bold text-gray-800">{question.title}</h1>
          <button
            onClick={confirmDeleteQuestion}
            className="px-5 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition"
          >
            질문 삭제
          </button>
        </div>
        <div className="flex flex-col sm:flex-row justify-between text-sm text-gray-500 mb-4">
          <p>작성자: {question.memberEmail}</p>
          <p>작성일: {new Date(question.createDate).toLocaleString()}</p>
        </div>
        <div className="prose max-w-none whitespace-pre-wrap text-gray-700">{question.content}</div>
      </div>

      <div className="bg-white rounded-lg shadow p-6">
        <h2 className="text-2xl font-bold text-gray-800 mb-4">
          {question.hasAnswer ? '답변 내용' : '답변 작성'}
        </h2>
        {question.answer && !isEditing ? (
          <div>
            <div className="prose max-w-none whitespace-pre-wrap text-gray-700 mb-4">
              {question.answer.content}
            </div>
            <div className="text-sm text-gray-500 mb-4">
              <p>작성일: {new Date(question.answer.createDate).toLocaleString()}</p>
              {question.answer.modifyDate && (
                <p>수정일: {new Date(question.answer.modifyDate).toLocaleString()}</p>
              )}
            </div>
            <div className="flex gap-3">
              <button
                onClick={() => {
                  setIsEditing(true);
                  setAnswerContent(question.answer.content);
                }}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
              >
                답변 수정
              </button>
              <button
                onClick={confirmDeleteAnswer}
                className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition"
              >
                답변 삭제
              </button>
            </div>
          </div>
        ) : (
          <form onSubmit={handleAnswerSubmit}>
            <textarea
              value={answerContent}
              onChange={(e) => setAnswerContent(e.target.value)}
              className="w-full h-48 p-4 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 mb-4"
              placeholder="답변 내용을 입력해주세요..."
              required
            />
            <div className="flex gap-3">
              <button
                type="submit"
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
              >
                {isEditing ? '답변 수정' : '답변 등록'}
              </button>
              {isEditing && (
                <button
                  type="button"
                  onClick={() => {
                    setIsEditing(false);
                    setAnswerContent(question.answer?.content || '');
                  }}
                  className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700 transition"
                >
                  취소
                </button>
              )}
            </div>
          </form>
        )}
      </div>
    </div>
  );
}
