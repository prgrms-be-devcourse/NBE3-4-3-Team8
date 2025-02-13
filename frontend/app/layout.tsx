import type { ReactNode } from 'react';
import './globals.css';
import Header from './components/NavBar';
import Footer from './components/Footer';
import { AuthProvider } from './hooks/useAuth';

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="ko">
      <body className="min-h-screen bg-white text-black flex flex-col">
        <AuthProvider>
          <Header />
          <main>{children}</main>
          <Footer />
        </AuthProvider>
      </body>
    </html>
  );
}