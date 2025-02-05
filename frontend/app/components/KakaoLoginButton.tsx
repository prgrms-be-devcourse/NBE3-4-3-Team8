"use client";

import { useRouter } from "next/navigation";

export default function KakaoLoginButton() {
  const router = useRouter();

  const handleLogin = () => {
    router.push("http://localhost:8080/oauth2/authorization/kakao");
  };

  return (
    <button
      onClick={handleLogin}
      className="flex items-center gap-2 bg-yellow-400 hover:bg-yellow-500 px-4 py-2 rounded text-white font-medium"
    >
      카카오로 시작하기
    </button>
  );
}