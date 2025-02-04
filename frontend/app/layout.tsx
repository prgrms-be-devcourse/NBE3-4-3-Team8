import type { ReactNode } from 'react';
import './globals.css';
import Header from './components/NavBar';
import Footer from './components/Footer';

export default function RootLayout({ children }: { children: ReactNode }) {
    return (
        <html lang="ko">
        <body className="min-h-screen bg-white text-black flex flex-col">
        <Header />
        <main className="max-w-7xl mx-auto px-4 flex-1">{children}</main>
        <Footer />
        </body>
        </html>
    );
}
