'use client';
import React, { useState, useEffect } from 'react';
import { useSearchParams, useRouter } from 'next/navigation';
import BookGrid from './components/BookGrid';
import { fetchSearchBooks } from '@/utils/api';
import { Book } from '@/types/book';
import { Pagination } from '@/app/components/common/Pagination';
import { SortBar } from './components/SortBar';

export default function SearchPage() {
  // URL 쿼리 파라미터에서 keyword, searchType, sort 값을 읽어옴
  const searchParams = useSearchParams();
  const router = useRouter();
  const keywordParam = searchParams.get('keyword') || '';
  const searchTypeParam = searchParams.get('searchType') || 'TITLE';
  const initialSort = searchParams.get('sort') || 'PUBLISHED_DATE';

  const [books, setBooks] = useState<Book[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [sortType, setSortType] = useState<string>(initialSort);

  const pageSize = 12;

  useEffect(() => {
    if (!keywordParam) return;

    const fetchBooks = async () => {
      try {
        // fetchSearchBooks가 searchType 파라미터도 받도록 수정했다고 가정
        const data = await fetchSearchBooks(
          currentPage,
          pageSize,
          sortType,
          searchTypeParam,
          keywordParam,
        );
        setBooks(data.content || data);
        setTotalPages(data.totalPages || 1);
      } catch (error) {
        console.error('도서 검색 중 오류 발생:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchBooks();
  }, [keywordParam, searchTypeParam, currentPage, sortType]);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    setLoading(true);
  };

  const handleSortChange = (newSort: string) => {
    setSortType(newSort);
    setCurrentPage(0);
    // URL 쿼리 파라미터 업데이트 (정렬 상태 유지)
    const params = new URLSearchParams(window.location.search);
    params.set('sort', newSort);
    router.push(`/search?${params.toString()}`);
  };

  if (loading) return <p className="text-center py-8">검색 결과 로딩 중...</p>;
  if (!books.length) return <p className="text-center py-8">검색 결과가 없습니다.</p>;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">검색 결과: "{keywordParam}"</h1>
      <SortBar currentSort={sortType} onSortChange={handleSortChange} />
      <BookGrid books={books} />
      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageChange}
      />
    </div>
  );
}
