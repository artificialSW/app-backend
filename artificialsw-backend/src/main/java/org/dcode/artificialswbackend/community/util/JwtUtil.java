package org.dcode.artificialswbackend.community.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {

    // Base64 인코딩된 시크릿 키 (32자 이상 바이트 길이 필요)
    private static final String SECRET_BASE64 = "gJHvvnNyUaa7Ge/YqN1nw9ozQakHBJq7WDc51awR5RM=";

    // Base64 디코딩 후 Key 객체 생성
    private static final Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_BASE64));

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // JWT 토큰에서 userId를 추출하고 유효성 검증
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

    // userId를 담아 JWT 토큰 생성 (1시간 유효)
    public static String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
}