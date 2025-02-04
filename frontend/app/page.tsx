"use client"
import { useRouter } from "next/navigation";
export default function Home() {

  const router = useRouter();
  return (
    <div>
      <a href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=1989bba61411e1765a90612b4d9de8a3&redirect_uri=http://localhost:8080/auth/login/kakao">
      카카오 로그인
      </a>

      <div className="text-center my-8" onClick={() => router.push("/my")}>
      마이페이지            
      </div>
    </div>
  );
}
