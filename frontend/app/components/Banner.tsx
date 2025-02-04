import React, { useState, useEffect } from 'react';
import Image from 'next/image';
import ArrowButton from './common/ArrowButton';

const Banner = () => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [images, setImages] = useState<string[]>([]);

  // 이미지 목록 조회 (스프링부트 API 연동 예시)
  useEffect(() => {
    const fetchImages = async () => {
      try {
        const response = await fetch('http://localhost:8080/event/banners');
        const data = await response.json();
        setImages(data);
      } catch (error) {
        console.error('Error fetching banners:', error);
      }
    };
    fetchImages();
  }, []);

  const handlePrevClick = () => {
    setCurrentIndex((prev) => (prev === 0 ? images.length - 1 : prev - 1));
  };

  const handleNextClick = () => {
    setCurrentIndex((prev) => (prev === images.length - 1 ? 0 : prev + 1));
  };

  // 3초마다 이미지 변경
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prev) => (prev === images.length - 1 ? 0 : prev + 1));
    }, 3000);

    return () => clearInterval(interval); // 컴포넌트 언마운트 시 인터벌 정리
  }, [images.length]);

  if (!images.length) return null;

  return (
    <div className="relative my-8 h-96 overflow-hidden">
      {images.map((img, index) => (
        <div
          key={img}
          className={`absolute inset-0 transition-opacity duration-500 ${
            index === currentIndex ? 'opacity-100' : 'opacity-0'
          }`}
        >
          <Image
            src={`http://localhost:8080${img}`}
            alt="event banner"
            fill
            className="object-cover"
            priority
            sizes="(max-width: 768px) 100vw, 80vw"
          />
        </div>
      ))}

      <ArrowButton direction="left" onClick={handlePrevClick} />
      <ArrowButton direction="right" onClick={handleNextClick} />
    </div>
  );
};

export default Banner;
