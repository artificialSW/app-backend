package org.dcode.artificialswbackend.community.util;

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

    public static Long validateAndGetFamilyId(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            // familyId는 Long 타입으로 저장했으니, Number로 받아서 Long으로 변환
            return claims.getBody().get("familyId", Number.class).longValue();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid or expired JWT token");
        }
    }

}