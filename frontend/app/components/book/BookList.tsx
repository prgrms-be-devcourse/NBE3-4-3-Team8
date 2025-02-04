'use client';

import Image from 'next/image';
import { useRef, useState } from 'react';
import ArrowButton from '../common/ArrowButton';
import NoImage from '@/app/components/common/NoImage';
import { useRouter } from 'next/navigation';

interface Book {
  id: number;
  title: string;
  author: string;
  price: number;
  stock: number;
  coverImage: string;
  pubDate: string;
  categoryId: number;
}

interface BookListProps {
  title: string;
  books: Book[];
}

export default function BookList({ title, books }: BookListProps) {
  const router = useRouter(); // 추가
  const scrollRef = useRef<HTMLDivElement>(null);
  const [showLeftButton, setShowLeftButton] = useState(false);
  const [showRightButton, setShowRightButton] = useState(true);

  const handleScroll = () => {
    if (scrollRef.current) {
      const { scrollLeft, scrollWidth, clientWidth } = scrollRef.current;
      setShowLeftButton(scrollLeft > 0);
      setShowRightButton(scrollLeft < scrollWidth - clientWidth - 10);
    }
  };

  const scroll = (direction: 'left' | 'right') => {
    if (scrollRef.current) {
      const scrollAmount = 800;
      scrollRef.current.scrollBy({
        left: direction === 'left' ? -scrollAmount : scrollAmount,
        behavior: 'smooth',
      });
    }
  };

  const handleBookClick = (bookId: number) => {
    router.push(`/books/${bookId}`);
  };

  return (
    <div className="w-full py-8">
      <h2 className="text-2xl font-bold mb-6 px-4">{title}</h2>
      <div className="relative px-4">
        {showLeftButton && <ArrowButton direction="left" onClick={() => scroll('left')} />}
        <div
          ref={scrollRef}
          onScroll={handleScroll}
          className="flex overflow-x-auto scrollbar-hide gap-5"
        >
          {books.map((book) => (
            <div
              key={book.id}
              className="book-card flex-none w-[240px] p-[14px] border rounded-lg shadow-md cursor-pointer hover:shadow-lg transition-shadow"
              onClick={() => handleBookClick(book.id)}
            >
              <div className="relative w-full aspect-[3/4.5]">
                {book.coverImage ? (
                  <Image
                    src={book.coverImage}
                    alt={book.title}
                    fill
                    sizes="240px"
                    className="rounded object-cover"
                    priority
                  />
                ) : (
                  <NoImage />
                )}
              </div>
            </div>
          ))}
        </div>
        {showRightButton && <ArrowButton direction="right" onClick={() => scroll('right')} />}
      </div>
    </div>
  );
}
