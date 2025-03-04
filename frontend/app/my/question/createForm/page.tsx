"use client"

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import {  ReqQuestionDto,QuestionDto  } from "./types";
import { PostQuestion } from "./api";
import Sidebar from '@/app/components/my/Sidebar';

export default function Home() {
    const router = useRouter();
    const [file, setFile] = useState(null);
    const [typeCode, setTypeCode] = useState('attachment');
    const [formData, setFormData] = useState<ReqQuestionDto>({
        title: "",
        content: "",
      });
    
      const [loading, setLoading] = useState(false); // 요청 중 상태
      const [message, setMessage] = useState<string | null>(null); // 성공/실패 메시지
    
      // 🔹 입력 값 변경 핸들러
      const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({
          ...formData,
          [e.target.name]: e.target.value,
        });
      };
    
      // 🔹 폼 제출 핸들러
      const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setMessage(null);

        if (!file || !typeCode) {
          setMessage('파일과 타입 코드를 모두 입력해주세요.');
          return;
        }
    
        try {
          const response= await PostQuestion(formData);
          if (!response.ok) throw new Error("서버 요청 실패!");
          
          const data: QuestionDto = await response.json();
          const questionId =data.id;

          const fileFormData = new FormData();
          fileFormData.append('file', file);
    
          const Fileresponse = await fetch(`http://localhost:8080/my/question/genFile/${questionId}/${typeCode}`, {
            method: 'POST',
            body: fileFormData,
            credentials: 'include', // 쿠키를 포함시키기 위해
          });
    
          if (!response.ok) {
            throw new Error('파일 업로드에 실패했습니다.');
          }
          setMessage("질문이 성공적으로 등록되었습니다!");
          setFormData({ title: "", content: "" }); // 입력 필드 초기화

          
          router.push("/my/question")
        } catch (error) {
          setMessage("질문 등록에 실패했습니다.");
        } finally {
          setLoading(false);
        }
      };

      const handleFileChange = (e) => {
        setFile(e.target.files[0]);
      };

      
    
      return (
        <div className="flex">
        <Sidebar />
        <main className="flex-1 p-6 relative">
        <div className="container mx-auto p-4 max-w-lg">
          <h1 className="text-2xl font-bold mb-4">질문 작성</h1>
          
          {message && <p className="mb-4 text-center text-red-500">{message}</p>}
          
          <form onSubmit={handleSubmit} className="space-y-4">

            <div>
              <label htmlFor="file">파일:</label>
              <input type="file" id="file" onChange={handleFileChange} required />
            </div>
            {/*
            <div>
              <label htmlFor="typeCode">타입 코드:</label>
              <input
                  type="text"
                  id="typeCode"
                  value={typeCode}
                  onChange={(e) => setTypeCode(e.target.value)}
                  required
              />
          </div>
          */}
          {/* 제목 입력 */}
            <input
              type="text"
              name="title"
              placeholder="제목을 입력하세요"
              value={formData.title}
              onChange={handleChange}
              required
              className="w-full border p-2 rounded-md"
            />
    
            {/* 내용 입력 */}
            <textarea
              name="content"
              placeholder="내용을 입력하세요"
              value={formData.content}
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
              {loading ? "등록 중..." : "질문 등록"}
            </button>
          </form>
        </div>
        </main>
        </div>
      );
    
}