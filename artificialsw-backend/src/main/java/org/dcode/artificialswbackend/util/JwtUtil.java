package org.dcode.artificialswbackend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 실제 서비스에서는 환경변수 등으로 관리

    public String generateToken(Long userId, Long familyId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("familyId", familyId)
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();
    }

    public static String validateAndGetUserId(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getSubject(); // subject를 userId로 사용
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid or expired JWT token");
        }
    }

    // 토큰 검증 등 추가 메서드 필요시 구현
}
