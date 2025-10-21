package org.dcode.artificialswbackend.tree;

import org.dcode.artificialswbackend.tree.dto.TreeFlowerResponseDto;
import org.dcode.artificialswbackend.community.entity.QuestionReference;
import org.dcode.artificialswbackend.community.CommunityService;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TreeController {

    private final TreeService treeService;
    private final CommunityService communityService;
    private final JwtUtil jwtUtil;

    public TreeController(TreeService treeService, CommunityService communityService, JwtUtil jwtUtil) {
        this.treeService = treeService;
        this.communityService = communityService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/tree/main/{year}/{month}/{period}/{position}/flower")
    public ResponseEntity<List<TreeFlowerResponseDto>> getTreeFlowersByParams(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int period,
            @PathVariable int position) {
        try {
            // JWT 토큰에서 Bearer 제거
            String token = authHeader.replace("Bearer ", "");
            
            // 토큰에서 familyId 추출
            Long familyId = jwtUtil.validateAndGetFamilyId(token);
            
            // 새로운 메서드로 꽃들 조회
            List<TreeFlowerResponseDto> flowers = treeService.getTreeFlowersByParams(familyId, year, month, period, position);
            return ResponseEntity.ok(flowers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tree/flower/{id}")
    public ResponseEntity<Object> getFlowerQuestion(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            // JWT 토큰에서 Bearer 제거
            String token = authHeader.replace("Bearer ", "");
            
            // 토큰에서 userId 추출
            Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));
            
            // 꽃 ID로 QuestionReference 조회
            QuestionReference questionRef = treeService.getFlowerQuestionReference(id);
            Long questionRefId = questionRef.getId();
            
            // 질문 타입에 따라 적절한 서비스 메서드 호출
            Object questionDetail;
            if (questionRef.getQuestionType() == QuestionReference.QuestionType.Personal) {
                questionDetail = communityService.getQuestionDetail(questionRefId, userId);
            } else if (questionRef.getQuestionType() == QuestionReference.QuestionType.Public) {
                questionDetail = communityService.getPublicQuestionDetail(questionRefId, userId);
            } else {
                throw new RuntimeException("Unknown question type: " + questionRef.getQuestionType());
            }
            
            return ResponseEntity.ok(questionDetail);
        } catch (Exception e) {
            // 에러 로그 출력
            System.err.println("Error in getFlowerQuestion: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/tree/flower/{id}/debug")
    public ResponseEntity<Object> debugFlowerQuestion(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            // JWT 토큰에서 Bearer 제거
            String token = authHeader.replace("Bearer ", "");
            
            // 토큰에서 userId 추출
            Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));
            
            // 디버깅 정보 수집
            java.util.Map<String, Object> debugInfo = new java.util.HashMap<>();
            debugInfo.put("flowerId", id);
            debugInfo.put("userId", userId);
            
            try {
                QuestionReference questionRef = treeService.getFlowerQuestionReference(id);
                debugInfo.put("questionRefId", questionRef.getId());
                debugInfo.put("questionType", questionRef.getQuestionType().toString());
                debugInfo.put("questionFamilyId", questionRef.getFamilyId());
            } catch (Exception e) {
                debugInfo.put("error", e.getMessage());
            }
            
            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Debug error: " + e.getMessage());
        }
    }
}