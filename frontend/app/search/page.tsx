"use client";

import React, { useState, useEffect } from "react";
import { useSearchParams } from "next/navigation";
import SearchResultItem from "./components/SearchResultItem";
import { fetchSearchBooks } from "@/utils/api.js";

interface Book {
    id: number;
    title: string;
    price: number;
    coverImage: string;
    description: string;
}

export default function SearchPage() {
    // URL 쿼리 파라미터에서 title 값을 읽어옴 (예: /books/search?title=김한민)
    const searchParams = useSearchParams();
    const titleParam = searchParams.get("title") || "";
    const [books, setBooks] = useState<Book[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!titleParam) return;

        const fetchBooks = async () => {
            try {
                // 백엔드 API 호출 (GET /books/search?title=검색어)
                const data = await fetchSearchBooks(0, 10, "PUBLISHED_DATE", titleParam);
                // 백엔드 응답이 페이지네이션 형태(content 필드가 있을 경우)
                setBooks(data.content || data);
            } catch (error) {
                console.error("도서 검색 중 오류 발생:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchBooks();
    }, [titleParam]);

    if (loading) return <p>검색 결과 로딩 중...</p>;
    if (!books.length) return <p>검색 결과가 없습니다.</p>;

    return (
        <div className="max-w-7xl mx-auto px-4 py-8">
            <h1 className="text-2xl font-bold mb-6">검색 결과: "{titleParam}"</h1>
            <div className="space-y-6">
                {books.map((book) => (
                    <SearchResultItem key={book.id} book={book} />
                ))}
            </div>
        </div>
    );
}
