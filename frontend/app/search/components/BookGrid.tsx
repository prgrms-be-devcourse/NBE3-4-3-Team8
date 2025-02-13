"use client";
import React from "react";
import { Book } from "@/types/book";
import BookCard from "./BookCard";

interface BookGridProps {
    books: Book[];
}

const BookGrid: React.FC<BookGridProps> = ({ books }) => {
    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8 justify-center">
            {books.map((book) => (
                <div key={book.id} className="flex justify-center">
                    <BookCard book={book} />
                </div>
            ))}
        </div>
    );
};

export default BookGrid;