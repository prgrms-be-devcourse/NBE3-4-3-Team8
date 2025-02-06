// components/common/Pagination.tsx
interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

export const Pagination = ({ currentPage, totalPages, onPageChange }: PaginationProps) => {
  if (totalPages <= 1) return null;

  const generatePageNumbers = (current: number, total: number) => {
    const pageGroupSize = 5;
    let startPage = 0;
    let endPage = 0;

    if (total <= pageGroupSize) {
      startPage = 0;
      endPage = total - 1;
    } else {
      if (current < 3) {
        startPage = 0;
        endPage = pageGroupSize - 1;
      } else if (current > total - 3) {
        startPage = total - pageGroupSize;
        endPage = total - 1;
      } else {
        startPage = current - 2;
        endPage = current + 2;
      }
    }

    return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
  };

  const pageNumbers = generatePageNumbers(currentPage, totalPages);

  return (
    <div className="flex justify-center mt-6 gap-2">
      <button
        onClick={() => onPageChange(0)}
        disabled={currentPage === 0}
        className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 disabled:bg-gray-100 disabled:text-gray-400"
      >
        &laquo;
      </button>

      <button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage === 0}
        className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 disabled:bg-gray-100 disabled:text-gray-400"
      >
        &lt;
      </button>

      {pageNumbers.map((pageNum) => (
        <button
          key={pageNum}
          onClick={() => onPageChange(pageNum)}
          className={`px-3 py-1 rounded ${
            currentPage === pageNum ? 'bg-blue-500 text-white' : 'bg-gray-200 hover:bg-gray-300'
          }`}
        >
          {pageNum + 1}
        </button>
      ))}

      <button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage === totalPages - 1}
        className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 disabled:bg-gray-100 disabled:text-gray-400"
      >
        &gt;
      </button>

      <button
        onClick={() => onPageChange(totalPages - 1)}
        disabled={currentPage === totalPages - 1}
        className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 disabled:bg-gray-100 disabled:text-gray-400"
      >
        &raquo;
      </button>
    </div>
  );
};
