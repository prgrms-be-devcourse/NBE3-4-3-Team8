// components/BookCard.tsx
export default function BookCard() {
    return (
        <div className="flex p-4 border rounded-lg">
            <div className="w-32 h-40 bg-gray-200 mr-4"></div>
            <div className="flex-1">
                <h2 className="text-lg font-bold">도서 제목</h2>
                <p className="text-sm text-gray-600">저자</p>
                <p className="text-sm text-gray-600">출판일</p>
                <div className="flex items-center mt-2">
                    <span className="text-red-500">10% {45000}원</span>
                    <span className="ml-2 line-through text-gray-400">{50000}원</span>
                </div>
                <div className="flex items-center mt-2 text-sm">
                    <span className="text-yellow-400">★</span>
                    <span className="ml-1">0.0 (0)</span>
                </div>
            </div>
            <div className="flex flex-col gap-2">
                <button className="px-4 py-2 bg-gray-100 rounded">장바구니</button>
                <button className="px-4 py-2 bg-gray-200 rounded">바로구매</button>
            </div>
        </div>
    );
}
