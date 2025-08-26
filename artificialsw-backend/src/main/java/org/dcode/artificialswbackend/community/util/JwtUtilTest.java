package org.dcode.artificialswbackend.community.util;

public class JwtUtilTest {
    public static void main(String[] args) {
        String userId = "3";  // 원하는 userId 입력
        String token = JwtUtil.generateToken(userId);
        System.out.println("생성된 JWT 토큰:");
        System.out.println(token);
    }
}
