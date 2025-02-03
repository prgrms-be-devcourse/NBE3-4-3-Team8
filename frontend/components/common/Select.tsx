// components/common/Select.tsx
interface SelectProps {
    defaultValue: string;
    options?: { label: string; value: string }[];
    onChange?: (value: string) => void;
}

export const Select = ({ defaultValue, options = [], onChange }: SelectProps) => {
    return (
        <select
            className="px-4 py-2 border rounded-md bg-white"
            onChange={(e) => onChange?.(e.target.value)}
        >
            <option value="">{defaultValue}</option>
            {options.map((option) => (
                <option key={option.value} value={option.value}>
                    {option.label}
                </option>
            ))}
        </select>
    );
};
