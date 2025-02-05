"use client";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function KakaoCallback() {
  const router = useRouter();

  useEffect(() => {
    const fetchUserAndRedirect = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/auth/me", {
          credentials: "include",
        });

        if (!response.ok) throw new Error("Failed to fetch user data");

        router.push("/my/profile"); // 프로필 페이지로 이동
      } catch (error) {
        console.error("Error fetching user data:", error);
        router.push("/"); // 로그인 실패 시 홈으로 이동
      }
    };

    fetchUserAndRedirect();
  }, [router]);

  return (
    <div className="flex min-h-screen items-center justify-center">
      <p>카카오 로그인 처리중...</p>
    </div>
  );
}
