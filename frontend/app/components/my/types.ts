// 📌 MemberType 열거형 (Enum)
export enum MemberType {
    USER = "USER",
    ADMIN = "ADMIN",
  }
  
  // 📌 MemberDto 인터페이스
  export interface MemberDto {
    id: number; // Long → number
    oAuthId: string;
    name: string;
    email: string;
    memberType: MemberType; // Enum 적용
  }