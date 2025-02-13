'use client';
import React, { useEffect, useState } from 'react';
import Image from 'next/image';
import ArrowButton from './common/ArrowButton';

const Banner = () => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [images, setImages] = useState<string[]>([]);

  useEffect(() => {
    const fetchImages = async () => {
      const cacheName = 'event-banners';
      const requestURL = 'http://localhost:8080/event/banners';

      // Cache Storage 열기
      const cache = await caches.open(cacheName);
      // 캐시에 저장된 응답이 있는지 확인
      const cachedResponse = await cache.match(requestURL);
      if (cachedResponse) {
        const data: string[] = await cachedResponse.json();
        // 캐시된 데이터가 존재하면 사용
        if (data && data.length > 0) {
          setImages(data);
          return;
        }
      }

      // 캐시에 데이터가 없거나 빈 배열이면 네트워크 요청 진행
      try {
        const response = await fetch(requestURL);
        if (!response.ok) {
          throw new Error('네트워크 응답이 올바르지 않습니다.');
        }

        // 응답을 clone()해서 캐시에 쓸 복제본을 만들어둠
        const responseToCache = response.clone();

        // 원본 응답(response)로부터 JSON 데이터 파싱
        const data: string[] = await response.json();
        setImages(data);

        // 복제본을 캐시에 put
        await cache.put(requestURL, responseToCache);
      } catch (error) {
        console.error('배너 이미지 로딩 중 오류 발생:', error);
      }
    };

    fetchImages();
  }, []);

  // 3초마다 자동 이미지 변경
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prev) => (prev === images.length - 1 ? 0 : prev + 1));
    }, 3000);
    return () => clearInterval(interval);
  }, [images.length]);

  const handlePrevClick = () => {
    setCurrentIndex((prev) => (prev === 0 ? images.length - 1 : prev - 1));
  };

  const handleNextClick = () => {
    setCurrentIndex((prev) => (prev === images.length - 1 ? 0 : prev + 1));
  };

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