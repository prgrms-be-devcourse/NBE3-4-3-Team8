'use client';

import BookList from '@/app/components/BookList';
// import { Select } from '@/app/components/common/Select';

export default function BooksPage() {
  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">'검색어'에 대한 xxx개의 검색 결과</h1>
      <div className="flex justify-end gap-4 mb-6">
        {/*<Select defaultValue="인기순" />*/}
        {/*<Select defaultValue="20개씩 보기" />*/}
      </div>
      {/*<BookList />*/}
    </div>
  );
}