import React from 'react';

const Banner = () => {
    return (
        <div className="relative my-8">
            <button className="absolute left-4 top-1/2 -translate-y-1/2 w-10 h-10 bg-white rounded-full border border-black flex items-center justify-center text-black">
                {'<'}
            </button>
            <div className="h-64 bg-gray-100 flex items-center justify-center text-2xl font-bold text-black">
                EVENT BANNER
            </div>
            <button className="absolute right-4 top-1/2 -translate-y-1/2 w-10 h-10 bg-white rounded-full border border-black flex items-center justify-center text-black">
                {'>'}
            </button>
        </div>
    );
};

export default Banner;
