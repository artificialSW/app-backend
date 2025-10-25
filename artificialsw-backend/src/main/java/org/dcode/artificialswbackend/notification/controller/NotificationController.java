package org.dcode.artificialswbackend.notification.controller;

import org.dcode.artificialswbackend.notification.dto.FcmTokenRequest;
import org.dcode.artificialswbackend.notification.dto.NotificationRequest;
import org.dcode.artificialswbackend.notification.service.FcmService;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    
    @Autowired
    private FcmService fcmService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register-token")
    public ResponseEntity<Map<String, String>> registerFcmToken(
            @RequestBody FcmTokenRequest request,
            HttpServletRequest httpServletRequest) {
        
        Map<String, String> response = new HashMap<>();
        
        try {
            // JWT에서 사용자 정보 추출
            String token = httpServletRequest.getHeader("Authorization").substring(7);
            Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));
            Long familyId = jwtUtil.validateAndGetFamilyId(token);
            
            // FCM 토큰 등록
            fcmService.registerToken(userId, familyId, request.getToken(), request.getDeviceType());
            
            response.put("status", "success");
            response.put("message", "FCM 토큰이 성공적으로 등록되었습니다.");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "FCM 토큰 등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendNotification(
            @RequestBody NotificationRequest request,
            HttpServletRequest httpServletRequest) {
        
        Map<String, String> response = new HashMap<>();
        
        try {
            // JWT에서 사용자 정보 추출
            String token = httpServletRequest.getHeader("Authorization").substring(7);
            Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));
            Long familyId = jwtUtil.validateAndGetFamilyId(token);
            
            if (request.getSendToFamily()) {
                // 가족 전체에게 알림 전송
                fcmService.sendNotificationToFamily(familyId, request.getTitle(), 
                                                  request.getBody(), request.getData());
                response.put("message", "가족 전체에게 알림을 전송했습니다.");
            } else if (request.getTargetUserId() != null) {
                // 특정 사용자에게 알림 전송
                fcmService.sendNotificationToUser(request.getTargetUserId(), request.getTitle(), 
                                                request.getBody(), request.getData());
                response.put("message", "특정 사용자에게 알림을 전송했습니다.");
            } else {
                // 본인에게 알림 전송 (테스트용)
                fcmService.sendNotificationToUser(userId, request.getTitle(), 
                                                request.getBody(), request.getData());
                response.put("message", "본인에게 알림을 전송했습니다.");
            }
            
            response.put("status", "success");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "알림 전송 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/remove-token")
    public ResponseEntity<Map<String, String>> removeFcmToken(
            @RequestBody FcmTokenRequest request,
            HttpServletRequest httpServletRequest) {
        
        Map<String, String> response = new HashMap<>();
        
        try {
            // JWT에서 사용자 정보 추출
            String token = httpServletRequest.getHeader("Authorization").substring(7);
            Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));
            
            // FCM 토큰 비활성화
            fcmService.deactivateToken(userId, request.getToken());
            
            response.put("status", "success");
            response.put("message", "FCM 토큰이 성공적으로 비활성화되었습니다.");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "FCM 토큰 비활성화 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}