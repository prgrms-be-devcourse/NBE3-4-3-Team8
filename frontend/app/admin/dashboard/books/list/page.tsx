'use client';

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Table from '@/app/components/admin/Table';
import Pagination from '@/app/components/admin/Pagination';
import { useRouter } from 'next/navigation';

const BookListPage = () => {
  const [books, setBooks] = useState([]);
  const [selectedBooks, setSelectedBooks] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [bookDetail, setBookDetail] = useState(null);
  const [isEditable, setIsEditable] = useState(false); // 서버에서 받은 수정 권한 여부
  const [editMode, setEditMode] = useState(false); // 상세/편집 모드 구분
  const [editedBook, setEditedBook] = useState(null); // 편집 중인 도서 정보
  const router = useRouter();

  // 도서 목록 조회
  useEffect(() => {
    fetchBooks();
  }, [currentPage]);

  const fetchBooks = async () => {
    setLoading(true);
    try {
      const response = await axios.get(
        `http://localhost:8080/admin/books?page=${currentPage - 1}&pageSize=10`,
        { withCredentials: true },
      );
      setBooks(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error('❌ 도서 조회 실패:', error.response?.data);
    } finally {
      setLoading(false);
    }
  };

  // 도서 상세 조회 및 모달 열기 (상세 보기 모드)
  const handleShowDetail = async (bookId: number) => {
    try {
      const response = await axios.get(`http://localhost:8080/admin/books/${bookId}`, {
        withCredentials: true,
      });
      console.log('✅ 도서 상세 조회 성공:', response.data);
      setBookDetail(response.data);
      setIsEditable(true); // 수정 권한 여부 (서버에서 판단)
      setEditMode(false); // 상세 보기 모드
      setIsModalOpen(true);
    } catch (error) {
      console.error('❌ 도서 상세 조회 실패:', error.response?.data);
    }
  };

  // 상세 모달에서 수정 버튼 클릭 시 편집 모드로 전환
  const handleEnterEditMode = () => {
    if (!isEditable) {
      alert('수정 권한이 없습니다.');
      return;
    }
    if (confirm('정말 수정하시겠습니까?')) {
      setEditMode(true);
      setEditedBook({ ...bookDetail });
    }
  };

  // 편집 모드에서 입력값 변경 처리
  const handleInputChange = (field, value) => {
    setEditedBook((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  // 수정 내용 저장 (API 호출)
  const handleSaveBook = async () => {
    try {
      const response = await axios.patch(
        `http://localhost:8080/admin/books/${bookDetail.id}`,
        editedBook,
        { withCredentials: true },
      );
      console.log('✅ 도서 수정 성공:', response.data);
      setBookDetail(response.data);
      setEditMode(false);
      alert('도서 정보가 업데이트되었습니다.');
    } catch (error) {
      console.error('❌ 도서 수정 실패:', error.response?.data);
      alert('수정에 실패하였습니다.');
    }
  };

  // 삭제
  const handleDeleteBook = async (bookId: number) => {
    if (!confirm('정말 이 도서를 삭제하시겠습니까?')) {
      return;
    }

    try {
      const response = await axios.delete(`http://localhost:8080/admin/books/${bookId}`, {
        withCredentials: true,
      });

      alert(response.data); // "도서가 성공적으로 삭제되었습니다."
      fetchBooks(); // 삭제 후 목록 새로고침
    } catch (error) {
      console.error('❌ 도서 삭제 실패:', error.response?.data);
      alert('도서 삭제에 실패했습니다.');
    }
  };

  // 편집 모드 취소
  const handleCancelEdit = () => {
    setEditMode(false);
    setEditedBook(null);
  };

  return (
    <div className="max-w-6xl mx-auto p-6">
      <h1 className="text-2xl font-semibold mb-4">도서 목록</h1>

      {/* 검색 및 버튼 */}
      <div className="flex justify-between mb-4">
        <input
          type="text"
          placeholder="ISBN, 제목, 저자 검색"
          className="border p-2 rounded-md w-1/3"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        <div className="flex gap-2">
          <button
            onClick={() => router.push('/admin/books/new')}
            className="bg-blue-500 text-white px-4 py-2 rounded"
          >
            등록
          </button>
          <button
            onClick={() => handleDeleteBook(bookDetail?.id)}
            className="bg-red-500 text-white px-4 py-2 rounded"
          >
            삭제
          </button>
        </div>
      </div>

      {/* 도서 목록 테이블 */}
      {loading ? (
        <div className="text-center text-gray-500">도서 목록을 불러오는 중...</div>
      ) : (
        <div>
          <Table
            columns={[
              { key: 'title', label: '제목' },
              { key: 'author', label: '저자' },
              { key: 'publisher', label: '출판사' },
              { key: 'pubDate', label: '출판일' },
              {
                key: '',
                label: '', // 버튼 텍스트 제거, 아이콘만 표시
                render: (book) => (
                  <button
                    onClick={() => handleShowDetail(book.id)}
                    className="bg-gray-300 px-2 py-1 rounded text-sm"
                  >
                    📖
                  </button>
                ),
              },
            ]}
            data={books.filter(
              (book) => book.title.includes(searchQuery) || book.author.includes(searchQuery),
            )}
            onSelect={(id) =>
              setSelectedBooks((prev) =>
                prev.includes(id) ? prev.filter((bookId) => bookId !== id) : [...prev, id],
              )
            }
          />
          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            onPageChange={setCurrentPage}
          />
        </div>
      )}

      {/* 모달: 상세보기 및 편집 모드 */}
      {isModalOpen && bookDetail && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center p-4 z-50">
          <div className="bg-white rounded-xl shadow-lg max-w-5xl w-full max-h-[90vh] overflow-y-auto">
            {/* 모달 헤더 */}
            <div className="flex justify-between items-center border-b p-4">
              <h2 className="text-2xl font-bold">
                {editMode ? (
                  <input
                    type="text"
                    value={editedBook.title}
                    onChange={(e) => handleInputChange('title', e.target.value)}
                    className="border-b focus:outline-none text-2xl font-bold max-w-full w-full"
                  />
                ) : (
                  bookDetail.title
                )}
              </h2>
              <div className="flex gap-2">
                {editMode ? (
                  <>
                    <button
                      onClick={handleSaveBook}
                      className="bg-green-500 text-white px-4 py-2 rounded w-30"
                    >
                      저장
                    </button>
                    <button
                      onClick={handleCancelEdit}
                      className="bg-gray-500 text-white px-4 py-2 rounded w-30"
                    >
                      취소
                    </button>
                  </>
                ) : (
                  <>
                    {isEditable && (
                      <button
                        onClick={handleEnterEditMode}
                        className="bg-yellow-500 text-white px-4 py-2 rounded"
                      >
                        수정
                      </button>
                    )}
                    <button
                      onClick={() => {
                        setIsModalOpen(false);
                        setEditMode(false);
                        setEditedBook(null);
                      }}
                      className="bg-gray-500 text-white px-4 py-2 rounded w-30"
                    >
                      닫기
                    </button>
                  </>
                )}
              </div>
            </div>

            {/* 모달 콘텐츠 */}
            <div className="p-4 grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* 왼쪽: 도서 표지 */}
              {bookDetail.coverImage && (
                <div className="flex justify-center items-center">
                  <img
                    src={bookDetail.coverImage}
                    alt="도서 표지"
                    className="w-80 h-auto rounded-md shadow"
                  />
                </div>
              )}
              {/* 오른쪽: 도서 상세 정보 */}
              <div className="space-y-4">
                {/* 저자 */}
                <div className="flex flex-col">
                  <label className="text-sm text-gray-600">저자</label>
                  {editMode ? (
                    <input
                      type="text"
                      value={editedBook.author}
                      onChange={(e) => handleInputChange('author', e.target.value)}
                      className="border rounded p-2"
                    />
                  ) : (
                    <p className="text-lg">{bookDetail.author}</p>
                  )}
                </div>
                {/* 출판사 */}
                <div className="flex flex-col">
                  <label className="text-sm text-gray-600">출판사</label>
                  {editMode ? (
                    <input
                      type="text"
                      value={editedBook.publisher}
                      onChange={(e) => handleInputChange('publisher', e.target.value)}
                      className="border rounded p-2"
                    />
                  ) : (
                    <p className="text-lg">{bookDetail.publisher}</p>
                  )}
                </div>
                {/* 출판일 (읽기 전용) */}
                <div className="flex flex-col">
                  <label className="text-sm text-gray-600">출판일</label>
                  <p className="text-lg">{bookDetail.pubDate}</p>
                </div>
                {/* 카테고리 */}
                <div className="flex flex-col">
                  <label className="text-sm text-gray-600">카테고리</label>
                  {editMode ? (
                    <input
                      type="text"
                      value={editedBook.category}
                      onChange={(e) => handleInputChange('category', e.target.value)}
                      className="border rounded p-2"
                    />
                  ) : (
                    <p className="text-lg">{bookDetail.category}</p>
                  )}
                </div>
                {/* ISBN (읽기 전용) */}
                <div className="flex flex-col">
                  <label className="text-sm text-gray-600">ISBN</label>
                  <p className="text-lg">{bookDetail.isbn}</p>
                </div>
                {/* 정가 (읽기 전용) */}
                <div className="flex flex-col">
                  <label className="text-sm text-gray-600">정가</label>
                  <p className="text-lg">{bookDetail.priceStandard} 원</p>
                </div>
                {/* 할인 가격 */}
                <div className="flex flex-col">
                  <label className="text-sm text-gray-600">할인 가격</label>
                  {editMode ? (
                    <input
                      type="number"
                      value={editedBook.pricesSales}
                      onChange={(e) => handleInputChange('pricesSales', e.target.value)}
                      className="border rounded p-2"
                    />
                  ) : (
                    <p className="text-lg">{bookDetail.pricesSales} 원</p>
                  )}
                </div>
                {/* 재고 */}
                <div className="flex flex-col">
                  <label className="text-sm text-gray-600">재고</label>
                  {editMode ? (
                    <input
                      type="number"
                      value={editedBook.stock}
                      onChange={(e) => handleInputChange('stock', e.target.value)}
                      className="border rounded p-2"
                    />
                  ) : (
                    <p className="text-lg">{bookDetail.stock}</p>
                  )}
                </div>
                {/* 판매 상태 */}
                <div className="flex flex-col">
                  <label className="text-sm text-gray-600">판매 상태</label>
                  {editMode ? (
                    <select
                      value={editedBook.status}
                      onChange={(e) => handleInputChange('status', e.target.value)}
                      className="border rounded p-2"
                    >
                      <option value={1}>판매중</option>
                      <option value={0}>판매 중지</option>
                    </select>
                  ) : (
                    <p className="text-lg">{bookDetail.status === 1 ? '판매중' : '판매 중지'}</p>
                  )}
                </div>
                {/* 평점 (읽기 전용) */}
                <div className="flex flex-col">
                  <label className="text-sm text-gray-600">평점</label>
                  <p className="text-lg">{bookDetail.rating} / 5</p>
                </div>
              </div>
            </div>

            {/* 목차 및 설명 영역 */}
            <div className="p-4 border-t space-y-6">
              <div>
                <h3 className="font-bold text-lg mb-2">📖 목차</h3>
                {editMode ? (
                  <textarea
                    value={editedBook.toc}
                    onChange={(e) => handleInputChange('toc', e.target.value)}
                    className="w-full border rounded p-2 h-24 resize-none"
                  />
                ) : (
                  <p className="whitespace-pre-wrap text-sm">{bookDetail.toc}</p>
                )}
              </div>
              <div>
                <h3 className="font-bold text-lg mb-2">📌 설명</h3>
                {editMode ? (
                  <textarea
                    value={editedBook.description}
                    onChange={(e) => handleInputChange('description', e.target.value)}
                    className="w-full border rounded p-2 h-32 resize-none"
                  />
                ) : (
                  <p className="whitespace-pre-wrap text-sm">{bookDetail.description}</p>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default BookListPage;
