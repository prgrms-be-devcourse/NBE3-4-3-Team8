import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export default function AdminPageLayout({ children }: { children: React.ReactNode }) {
    return (
        <>
            {children}
            <ToastContainer position="top-right" autoClose={5000} />
        </>
    );
}