// components/BookList.tsx
import BookCard from './BookCard';

export default function BookList() {
    return (
        <div className="space-y-4">
            {[1, 2, 3].map((book) => (
                <BookCard key={book} />
            ))}
        </div>
    );
}
