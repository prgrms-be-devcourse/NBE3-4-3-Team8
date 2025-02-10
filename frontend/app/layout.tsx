import type { ReactNode } from 'react';
import './globals.css';
import Header from './components/NavBar';
import Footer from './components/Footer';

export default function RootLayout({ children }: { children: ReactNode }) {
    return (
        <html lang="ko">
        <body className="min-h-screen bg-white text-black flex flex-col">
        <Header />
<<<<<<< HEAD
        <main >{children}</main>
        
=======
        <main>{children}</main>
>>>>>>> 5ee7eebc425604cde2cef208c325cb58bbbb69de
        <Footer />
        </body>
        </html>
    );
}
