import React from 'react';

interface ConfirmationModalProps {
  title: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
}

const ConfirmationModal: React.FC<ConfirmationModalProps> = ({
  title,
  message,
  onConfirm,
  onCancel,
}) => {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white rounded-lg shadow-lg p-6 max-w-md w-full">
        <h2 className="text-xl font-bold mb-4">{title}</h2>
        <p className="mb-6">{message}</p>
        <div className="flex justify-end gap-4">
          <button onClick={onCancel} className="px-4 py-2 bg-gray-500 text-white rounded">
            취소
          </button>
          <button onClick={onConfirm} className="px-4 py-2 bg-red-600 text-white rounded">
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmationModal;
