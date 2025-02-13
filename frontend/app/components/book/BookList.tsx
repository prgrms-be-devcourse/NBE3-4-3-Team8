'use client';

import Image from 'next/image';
import { useRef, useState } from 'react';
import ArrowButton from '../common/ArrowButton';
import NoImage from '@/app/components/common/NoImage';
import { useRouter } from 'next/navigation';
import { Book } from '@/types/book';

interface BookListProps {
  title: string;
  books: Book[];
}

export default function BookList({ title, books }: BookListProps) {
  const router = useRouter();
  const scrollRef = useRef<HTMLDivElement>(null);
  const [showLeftButton, setShowLeftButton] = useState(false);
  const [showRightButton, setShowRightButton] = useState(true);
  const [imageErrors, setImageErrors] = useState<{ [key: number]: boolean }>({});

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

  const handleImageError = (bookId: number) => {
    setImageErrors((prev) => ({
      ...prev,
      [bookId]: true,
    }));
  };

  const isValidImageUrl = (url: string) => {
    try {
      new URL(url);
      return true;
    } catch {
      return false;
    }
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
                {book.coverImage && isValidImageUrl(book.coverImage) && !imageErrors[book.id] ? (
                  <Image
                    src={book.coverImage}
                    alt={book.title}
                    fill
                    sizes="240px"
                    className="rounded object-cover"
                    priority
                    onError={() => handleImageError(book.id)}
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