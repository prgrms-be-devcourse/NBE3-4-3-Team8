import React from 'react';

interface Column {
  key: string;
  label: string;
  render?: (row: any) => React.ReactNode; // render 함수 추가
}

interface TableProps {
  columns: Column[]; // 수정된 타입: render를 포함한 Column 타입 사용
  data: any[];
  onSelect: (id: number) => void;
}

const Table: React.FC<TableProps> = ({ columns, data, onSelect }) => {
  return (
    <div className="overflow-x-auto rounded-lg shadow-md border border-gray-200">
      <table className="min-w-full bg-white border-collapse">
        <thead className="bg-gray-100">
          <tr>
            <th className="px-4 py-2 border">
              <input type="checkbox" />
            </th>
            {columns.map((col) => (
              <th key={col.key} className="px-4 py-2 text-left border">
                {col.label} {/* 한글 라벨 사용 */}
              </th>
            ))}
            <th className="px-4 py-2 border">Actions</th>
          </tr>
        </thead>
        <tbody>
          {data.map((row, index) => (
            <tr key={index} className="hover:bg-gray-50">
              <td className="px-4 py-2 border">
                <input type="checkbox" onChange={() => onSelect(row.id)} />
              </td>
              {columns.map((col) => (
                <td key={col.key} className="px-4 py-2 border">
                  {/* render 함수가 있으면 이를 사용하고, 없으면 기본 데이터 출력 */}
                  {col.render ? col.render(row) : (row[col.key] ?? '데이터 없음')}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Table;
