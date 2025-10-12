package org.dcode.artificialswbackend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        // Base64로 인코딩된 시크릿키를 디코딩하여 Key 객체 생성
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);

    }


    public String generateToken(Long userId, Long familyId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("familyId", familyId)
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();
    }

    public String validateAndGetUserId(String token) {
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

    public Long validateAndGetFamilyId(String token) {
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
