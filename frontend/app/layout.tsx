import type { ReactNode } from 'react';
import './globals.css';
import Header from './components/NavBar';
import Footer from './components/Footer';

export default function RootLayout({ children }: { children: ReactNode }) {
    return (
        <html lang="ko">
        <body className="min-h-screen bg-white text-black flex flex-col">
        <Header />
        <main>{children}</main>
        <Footer />
        </body>
        </html>
    );
}
