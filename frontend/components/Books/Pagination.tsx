// components/Books/Pagination.tsx
interface PaginationProps {
    currentPage: number;
    totalPages: number;
    onPageChange: (page: number) => void;
}

export const Pagination = ({ currentPage, totalPages, onPageChange }: PaginationProps) => {
    return (
        <div className="flex justify-center gap-2 mt-8">
            {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
                <button
                    key={page}
                    onClick={() => onPageChange(page)}
                    className={`px-4 py-2 rounded-md ${
                        currentPage === page
                            ? 'bg-gray-800 text-white'
                            : 'bg-gray-100'
                    }`}
                >
                    {page}
                </button>
            ))}
        </div>
    );
};
