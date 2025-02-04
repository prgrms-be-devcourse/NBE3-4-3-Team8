// types/book.ts
export interface Book {
  id: number;
  title: string;
  author: string;
  priceStandard: number;
  priceSales: number;
  stock: number;
  coverImage: string;
  pubDate: string;
  categoryId: number;
  rating: number;
  reviewCount: number;
  publisher: string;
  description: string;
}
