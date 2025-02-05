// books/[id]/page.tsx
import React from 'react';
import { BookInfo } from '@/app/components/book/BookInfo';
import { BookTabs } from '@/app/components/book/BookTabs';
import type { Book } from '@/types/book';

interface BookDetailPageProps {
  params: Promise<{ id: string }>; // 1. Promise 타입으로 변경
}

export default async function BookDetailPage({ params }: BookDetailPageProps) {
  const { id } = await params; // 2. params await 처리
  const response = await fetch(`http://localhost:8080/books/${id}`, {
    cache: 'no-store',
  });

  if (!response.ok) throw new Error('도서 정보를 불러오지 못했습니다');

  const bookData: Book = await response.json();

  return (
    <div className="min-h-screen bg-white">
      <BookInfo book={bookData} />
      <BookTabs bookId={bookData.id} />
    </div>
  );
}
