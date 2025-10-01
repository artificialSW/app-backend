package org.dcode.artificialswbackend.community.util;

public class JwtTokenGenerator {
    public static void main(String[] args) {
        // 테스트용 JWT 토큰 생성
        String userId1 = "1";
        String userId2 = "2";
        
        String token1 = JwtUtil.generateToken(userId1);
        String token2 = JwtUtil.generateToken(userId2);
        
        System.out.println("=== JWT 토큰 생성 완료 ===");
        System.out.println("User ID 1 토큰:");
        System.out.println(token1);
        System.out.println();
        System.out.println("User ID 2 토큰:");
        System.out.println(token2);
        System.out.println();
        
        // 토큰 검증 테스트
        try {
            String extractedUserId1 = JwtUtil.validateAndGetUserId(token1);
            String extractedUserId2 = JwtUtil.validateAndGetUserId(token2);
            
            System.out.println("=== 토큰 검증 결과 ===");
            System.out.println("토큰1에서 추출된 User ID: " + extractedUserId1);
            System.out.println("토큰2에서 추출된 User ID: " + extractedUserId2);
        } catch (Exception e) {
            System.out.println("토큰 검증 실패: " + e.getMessage());
        }
    }
}