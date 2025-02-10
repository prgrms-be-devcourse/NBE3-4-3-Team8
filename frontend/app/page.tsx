'use client';
import React from 'react';
import Banner from './components/Banner';
import BookList from './components/book/BookList';
import { useEffect, useState } from 'react';
import { Book } from '@/types/book';
import { saveTokenToCookie } from '@/utils/auth';
import { useSearchParams } from 'next/navigation';

interface PageResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
  };
  totalPages: number;
  totalElements: number;
}

export default function HomePage() {
  const [trendingBooks, setTrendingBooks] = useState<Book[]>([]);
  const [newBooks, setNewBooks] = useState<Book[]>([]);
  const searchParams = useSearchParams();

  useEffect(() => {
    const fetchBooks = async () => {

      const token = searchParams.get('token');
      if (token) {
        saveTokenToCookie(token);
      }
      
      try {
        const newBooksResponse = await fetch(
          'http://localhost:8080/books?sortType=PUBLISHED_DATE',
          {
            credentials: 'include',
          },
        );
        const newBooksData: PageResponse<Book> = await newBooksResponse.json();
        setNewBooks(newBooksData.content);

        const trendingBooksResponse = await fetch('http://localhost:8080/books?sortType=RATING', {
          credentials: 'include',
        });
        const trendingBooksData: PageResponse<Book> = await trendingBooksResponse.json();
        setTrendingBooks(trendingBooksData.content);
      } catch (error) {
        console.error('도서 데이터 로딩 중 오류 발생:', error);
      }
    };

    fetchBooks();
  }, []);

  return (
    <>
      <Banner />
      <BookList title="이달의 신작" books={newBooks} />
      <BookList title="급상승! 많이 보고 있는 상품" books={trendingBooks} />
    </>
  );
}
