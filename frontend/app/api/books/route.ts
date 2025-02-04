// app/api/books/route.ts
import { NextResponse } from 'next/server';

export async function GET(request: Request) {
    const { searchParams } = new URL(request.url);
    const sortType = searchParams.get('sortType');

    const books = Array(5).fill(null).map((_, i) => ({
        id: `book-${i + 1}`,
        title: '책 제목',
        image: 'Book Image',
        salesCount: Math.floor(Math.random() * 1000)
    }));

    if (sortType === 'SALES_COUNT') {
        books.sort((a, b) => (b.salesCount || 0) - (a.salesCount || 0));
    }

    return NextResponse.json(books);
}
