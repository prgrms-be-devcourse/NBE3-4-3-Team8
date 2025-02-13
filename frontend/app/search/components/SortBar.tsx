"use client";
import React from "react";

interface SortBarProps {
  currentSort: string;
  onSortChange: (newSort: string) => void;
}

const sortOptions = [
  { label: "출간일순", value: "PUBLISHED_DATE" },
  { label: "판매량순", value: "SALES_POINT" },
  { label: "평점순", value: "RATING" },
  { label: "리뷰 많은 순", value: "REVIEW_COUNT" },
];

export const SortBar: React.FC<SortBarProps> = ({ currentSort, onSortChange }) => {
  return (
      <div className="mt-4 border-b border-gray-300">
        <div className="flex">
          {sortOptions.map((option) => (
              <button
                  key={option.value}
                  onClick={() => onSortChange(option.value)}
                  className={`
              px-4 py-2 text-sm font-medium transition-colors
              ${currentSort === option.value
                      ? "border-t border-l border-r border-blue-500 bg-white text-blue-500"
                      : "text-gray-500 hover:text-blue-500"}
            `}
              >
                {option.label}
              </button>
          ))}
        </div>
      </div>
  );
};