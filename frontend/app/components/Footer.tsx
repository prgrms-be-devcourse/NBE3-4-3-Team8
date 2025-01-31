import React from 'react';

const Footer = () => {
    return (
        <footer className="border-t border-black py-4 text-center text-sm text-black">
            <div className="max-w-7xl mx-auto px-4">
                <p>© 2025 The Book. All rights reserved.</p>
                <div className="flex justify-center gap-6 mt-2">
                    <span className="cursor-pointer">이용약관</span>
                    <span className="cursor-pointer">개인정보 보호정책</span>
                    <span className="cursor-pointer">고객센터</span>
                </div>
            </div>
        </footer>
    );
};

export default Footer;
