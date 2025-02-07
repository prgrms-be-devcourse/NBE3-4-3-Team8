package com.ll.nbe342team8.domain.jwt;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Component
public class JwtService {
    @Value("${custom.jwt.secret}")
    private String secretKey;

    private static final long TOKEN_VALIDITY = 24 * 60 * 60 * 1000;
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    public String generateToken(Member member) {
        // 현재 시간과 만료 시간을 설정합니다
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDITY);

        // JWT 토큰을 생성합니다. 이때 카카오 ID를 주요 식별자로 사용합니다
        return Jwts.builder()
                .setSubject(member.getOauthId())  // 카카오 ID를 토큰의 주체로 설정
                .claim("id", member.getId())      // 내부 DB ID는 부가 정보로 포함
                .claim("email", member.getEmail())
                .claim("name", member.getName())
                .setIssuedAt(now)                 // 토큰 발급 시간
                .setExpiration(validity)          // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰에서 카카오 ID를 추출하는 메서드를 추가합니다
    public String getKakaoIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // setSubject()로 설정한 카카오 ID를 가져옵니다
    }

    // 토큰의 유효성을 검증하는 메서드도 추가합니다
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isTokenValid(String token) {
        return !tokenBlacklist.contains(token);
    }

}