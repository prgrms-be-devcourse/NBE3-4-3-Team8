'use client';
import { useEffect, useState } from 'react';
import Image from 'next/image';
import Link from 'next/link';
import { useAuth } from '@/app/hooks/useAuth';

const Sidebar = () => {
  const { user, logout } = useAuth();
  const [imgSrc, setImgSrc] = useState(user?.profileImageUrl);
  const [imgError, setImgError] = useState(false);

  // 사용자 정보가 변경될 때마다 이미지 소스 업데이트
  useEffect(() => {
    if (user?.profileImageUrl && !imgError) {
      setImgSrc(imgSrc);
    }
  }, [user, imgError]);

  // 이미지 로드 오류 처리
  const handleImageError = () => {
    setImgError(true);
  };

  return (
      <aside className="w-64 p-4 bg-gray-100 min-h-screen">
        <div className="flex flex-col items-center mb-6">
          {/* Next.js 13 이상에서는 데이터 URI 사용 시 width, height가 필요하고 unoptimized prop 추가 필요 */}
          <Image
              src={imgSrc}
              alt="User Profile"
              width={80}
              height={80}
              className="rounded-full border border-gray-300"
              onError={handleImageError}
              unoptimized={imgSrc.startsWith('data:')} // 데이터 URI는 Next.js의 이미지 최적화 건너뛰기
          />
          <p className="mt-2 font-semibold">{user?.name || '게스트'}</p>
        </div>
        <nav>
          <ul className="space-y-4">
            <li>
              <Link href="/my/orders" className="block p-2 bg-white rounded shadow">
                쇼핑내역
              </Link>
            </li>
            <li>
              <Link href="/my/reviews" className="block p-2 bg-white rounded shadow">
                리뷰내역
              </Link>
            </li>
            <li>
              <Link href="/my/question" className="block p-2 bg-white rounded shadow">
                문의내역
              </Link>
            </li>
            <li>
              <Link href="/my" className="block p-2 bg-white rounded shadow">
                회원정보
              </Link>
            </li>
          </ul>
        </nav>
      </aside>
  );
};

export default Sidebar;