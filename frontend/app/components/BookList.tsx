import React from 'react';
import Link from 'next/link';

interface Book {
    id: string;
    title: string;
    image: string;
}

interface BookListProps {
    title: string;
    books: Book[];
}

const BookList: React.FC<BookListProps> = ({ title, books }) => {
    return (
        <section className="my-12">
            <h2 className="text-lg font-bold mb-6 text-black">{title}</h2>
            <div className="grid grid-cols-5 gap-6">
                {books.map((book) => (
                    <Link href={`/book/${book.id}`} key={book.id}>
                        <div className="border border-black p-4 aspect-square text-black hover:shadow-lg transition-shadow cursor-pointer">
                            <div className="h-4/5 bg-gray-100 mb-2 flex items-center justify-center">
                                {book.image}
                            </div>
                            <div className="text-sm text-center">{book.title}</div>
                        </div>
                    </Link>
                ))}
            </div>
        </section>
    );
};

export  default BookList;