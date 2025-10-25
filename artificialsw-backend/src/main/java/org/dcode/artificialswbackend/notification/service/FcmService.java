package org.dcode.artificialswbackend.notification.service;

import org.dcode.artificialswbackend.notification.entity.FcmToken;
import org.dcode.artificialswbackend.notification.repository.FcmTokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FcmService {

    private final FcmTokenRepository fcmTokenRepository;

    public FcmService(FcmTokenRepository fcmTokenRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
    }

    // FCM 토큰 등록
    public void registerToken(Long userId, Long familyId, String token, String deviceType) {
        // 기존 토큰이 있는지 확인
        fcmTokenRepository.findByUserIdAndToken(userId, token)
                .ifPresentOrElse(
                    existingToken -> {
                        existingToken.setIsActive(true);
                        existingToken.setDeviceType(deviceType);
                        fcmTokenRepository.save(existingToken);
                    },
                    () -> {
                        FcmToken newToken = new FcmToken(userId, familyId, token, deviceType);
                        fcmTokenRepository.save(newToken);
                    }
                );
    }

    // 개별 사용자에게 알림 전송 (일단 로그만)
    public void sendNotificationToUser(Long userId, String title, String body, String data) {
        List<FcmToken> tokens = fcmTokenRepository.findByUserIdAndIsActiveTrue(userId);
        
        if (!tokens.isEmpty()) {
            System.out.println("📱 [FCM] 사용자 " + userId + "에게 알림: " + title + " - " + body);
            System.out.println("📱 [FCM] 등록된 토큰 수: " + tokens.size());
            
            // TODO: Firebase 설정 완료 후 실제 전송 로직 추가
        } else {
            System.out.println("📱 [FCM] 사용자 " + userId + "의 활성 토큰이 없습니다.");
        }
    }

    // 가족 전체에게 알림 전송 (일단 로그만)
    public void sendNotificationToFamily(Long familyId, String title, String body, String data) {
        List<FcmToken> tokens = fcmTokenRepository.findByFamilyIdAndIsActiveTrue(familyId);
        
        if (!tokens.isEmpty()) {
            System.out.println("👨‍👩‍👧‍👦 [FCM] 가족 " + familyId + "에게 알림: " + title + " - " + body);
            System.out.println("👨‍👩‍👧‍👦 [FCM] 등록된 토큰 수: " + tokens.size());
            
            // TODO: Firebase 설정 완료 후 실제 전송 로직 추가
        } else {
            System.out.println("👨‍👩‍👧‍👦 [FCM] 가족 " + familyId + "의 활성 토큰이 없습니다.");
        }
    }

    // 토큰 삭제
    public void deleteToken(Long userId, String token) {
        fcmTokenRepository.deleteByUserIdAndToken(userId, token);
    }
    
    // 토큰 비활성화
    public void deactivateToken(Long userId, String token) {
        System.out.println("📱 [FCM] 토큰 비활성화 - userId: " + userId + ", token: " + token.substring(0, 10) + "...");
        
        // 사용자의 특정 토큰을 비활성화
        Optional<FcmToken> existingToken = fcmTokenRepository.findByUserIdAndToken(userId, token);
        if (existingToken.isPresent()) {
            FcmToken fcmToken = existingToken.get();
            fcmToken.setIsActive(false);
            fcmTokenRepository.save(fcmToken);
            System.out.println("📱 [FCM] 토큰 비활성화 완료");
        } else {
            System.out.println("⚠️ [FCM] 비활성화할 토큰을 찾을 수 없습니다");
        }
    }
}