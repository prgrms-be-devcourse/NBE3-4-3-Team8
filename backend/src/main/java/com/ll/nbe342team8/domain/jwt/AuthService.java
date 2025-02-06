package com.ll.nbe342team8.domain.jwt;

import com.ll.nbe342team8.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    public ResponseEntity<?> authenticate(String token) {
        if (token == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        if (!token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Invalid token format");
        }

        String actualToken = token.substring(7);
        if (!jwtService.validateToken(actualToken)) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        String kakaoId = jwtService.getKakaoIdFromToken(actualToken);
        Optional<Member> memberOptional = memberRepository.findByOauthId(kakaoId);

        if (memberOptional.isEmpty()) {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(memberOptional.get());
    }
}
