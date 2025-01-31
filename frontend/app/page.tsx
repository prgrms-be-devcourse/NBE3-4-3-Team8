import React from 'react';
import Banner  from './components/Banner';
import BookList from './components/BookList';

const books = Array(5).fill(null).map((_, i) => ({
    id: `book-${i + 1}`,
    title: '책 제목',
    image: 'Book Image'
}));

export default function HomePage() {
    return (
        <>
            <Banner />
            <BookList title="이달의 신작" books={books} />
            <BookList title="급상승! 많이 보고 있는 상품" books={books} />
        </>
    );
}