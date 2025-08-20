package org.dcode.artificialswbackend.community.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // 실제 서비스 시에는 이 키를 안전하게 관리해야 합니다.
    private static final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret"; // 32자 이상 권장
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간 (밀리초 단위)

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