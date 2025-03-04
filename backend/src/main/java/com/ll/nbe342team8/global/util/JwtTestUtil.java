package com.ll.nbe342team8.global.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtTestUtil {

    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // ✅ 테스트용 키 생성

    public static String createTestJwtToken(String oauthId, Long memberId, String email, String name) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // ✅ 1시간 후 만료

        return Jwts.builder()
                .setSubject(oauthId)
                .claim("id", memberId)
                .claim("email", email)
                .claim("name", name)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key) // ✅ 테스트용 키 사용
                .compact();
    }
}
