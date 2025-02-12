'use client';

import React, {useState} from 'react';
import axios from 'axios';

const BookRegistrationPage = () => {
    const [isbn, setIsbn] = useState('');
    const [searchedBook, setSearchedBook] = useState<any>(null);
    const [loading, setLoading] = useState(false);

    // ISBN13로 도서 검색 (외부 API에서 도서 정보를 가져옴)
    const handleSearchBook = async () => {
        if (!isbn.trim()) {
            alert('ISBN13 값을 입력하세요.');
            return;
        }
        setLoading(true);
        try {
            // 관리자 도서 검색 API (AdminBookSearchDto: title, author, isbn13)
            const response = await axios.post(
                `http://localhost:8080/admin/books/search`,
                {title: '', author: '', isbn13: isbn},
                {withCredentials: true},
            );
            console.log('✅ 도서 검색 성공:', response.data);
            // 검색 결과는 무조건 하나로 가정 → 첫 번째 결과를 사용
            if (response.data) {
                setSearchedBook(response.data);
            } else {
                alert('검색 결과가 없습니다.');
                setSearchedBook(null);
            }
        } catch (error) {
            console.error('❌ 도서 검색 실패:', error.response?.data);
            alert('도서 검색에 실패했습니다.');
            setSearchedBook(null);
        } finally {
            setLoading(false);
        }
    };

    const handleRegisterBook = async () => {
        if (!searchedBook) {
            alert('먼저 도서를 검색하세요.');
            return;
        }

        try {
            const response = await axios.post(
                `http://localhost:8080/admin/books/register`,
                {isbn13: searchedBook.isbn13},
                {
                    withCredentials: true, // ✅ `HttpOnly` 쿠키를 요청에 자동으로 포함
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            );

            console.log("✅ 도서 등록 성공:", response.data);
            alert(response.data);
            setSearchedBook(null);
            setIsbn("");
        } catch (error) {
            console.error("❌ 도서 등록 실패:", error);

            if (error.response) {
                console.error("📌 상태 코드:", error.response.status);
                console.error("📌 응답 데이터:", error.response.data);
                alert(`도서 등록 실패: ${error.response.data}`);
            } else if (error.request) {
                console.error("📌 요청은 전송되었지만 응답이 없음:", error.request);
                alert("도서 등록 실패: 서버에서 응답이 없습니다.");
            } else {
                console.error("📌 요청 설정 중 오류 발생:", error.message);
                alert(`도서 등록 실패: ${error.message}`);
            }
        }
    };


    // 초기화 버튼 클릭 시 검색 결과와 입력값 초기화
    const handleReset = () => {
        setSearchedBook(null);
        setIsbn('');
    };

    return (
        <div className="max-w-6xl mx-auto p-6">
            <h1 className="text-2xl font-semibold mb-4">도서 등록</h1>

            {/* ISBN13 검색 입력 및 버튼 */}
            <div className="mb-4 flex items-center">
                <input
                    type="text"
                    placeholder="ISBN13 검색"
                    value={isbn}
                    onChange={(e) => setIsbn(e.target.value)}
                    className="border p-2 rounded-md w-1/3 mr-2"
                />
                <button
                    onClick={handleSearchBook}
                    className="bg-blue-500 text-white px-4 py-2 rounded"
                    disabled={loading}
                >
                    {loading ? '검색 중...' : '검색'}
                </button>
            </div>

            {/* 검색 결과(단일 도서 상세 정보) 표시 */}
            {searchedBook && (
                <div className="bg-white rounded-xl shadow-lg w-full p-6 mb-4 border">
                    <h2 className="text-2xl font-bold mb-4">{searchedBook.title}</h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {searchedBook.coverImage && (
                            <div className="flex justify-center">
                                <img
                                    src={searchedBook.coverImage}
                                    alt="도서 표지"
                                    className="w-32 h-auto rounded"
                                />
                            </div>
                        )}
                        <div className="space-y-2">
                            <p>
                                <strong>저자:</strong> {searchedBook.author}
                            </p>
                            <p>
                                <strong>출판사:</strong> {searchedBook.publisher}
                            </p>
                            <p>
                                <strong>출판일:</strong> {searchedBook.pubDate}
                            </p>
                            <p>
                                <p>
                                    <strong>카테고리:</strong> {searchedBook.category?.categoryName ?? '없음'}
                                </p>
                            </p>
                            <p>
                                <strong>ISBN13:</strong> {searchedBook.isbn13}
                            </p>
                            <p>
                                <strong>정가:</strong> {searchedBook.priceStandard} 원
                            </p>
                            <p>
                                <strong>할인 가격:</strong> {searchedBook.pricesSales} 원
                            </p>
                            <p>
                                <strong>재고:</strong> {searchedBook.stock}
                            </p>
                            <p>
                                <strong>판매 상태:</strong> {searchedBook.status === 1 ? '판매중' : '판매 중지'}
                            </p>
                            <p>
                                <strong>평점:</strong> {searchedBook.rating} / 5
                            </p>
                        </div>
                    </div>
                    {searchedBook.toc && (
                        <div className="mt-4">
                            <h3 className="font-bold mb-2">📖 목차</h3>
                            <p className="whitespace-pre-wrap text-sm">{searchedBook.toc}</p>
                        </div>
                    )}
                    <div className="mt-4">
                        <h3 className="font-bold mb-2">📌 설명</h3>
                        <p className="whitespace-pre-wrap text-sm">{searchedBook.description}</p>
                    </div>
                </div>
            )}

            {/* 등록 및 초기화 버튼 */}
            {searchedBook && (
                <div className="flex gap-4">
                    <button
                        onClick={handleRegisterBook}
                        className="bg-green-500 text-white px-4 py-2 rounded"
                    >
                        등록
                    </button>
                    <button onClick={handleReset} className="bg-gray-500 text-white px-4 py-2 rounded">
                        초기화
                    </button>
                </div>
            )}
        </div>
    );
};

export default BookRegistrationPage;
