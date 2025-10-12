package org.dcode.artificialswbackend.community;
import org.dcode.artificialswbackend.community.dto.CommentRequestDto;
import org.dcode.artificialswbackend.community.dto.LikeRequestDto;
import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
import org.dcode.artificialswbackend.community.dto.QuestionCreateRequestDto;
import org.dcode.artificialswbackend.community.dto.QuestionDetailResponseDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionResponseDto;
import org.dcode.artificialswbackend.community.dto.CommentResponseDto;
import org.dcode.artificialswbackend.community.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CommunityController {
    private final CommunityService communityService;
    private final JwtUtil jwtUtil;

    public CommunityController(CommunityService communityService,  JwtUtil jwtUtil) {
        this.communityService = communityService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/api/community/home/personal")
    public Map<String,Object> getPersonalCommunity(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.validateAndGetUserId(token);
        Long receiverId = Long.valueOf(userId);
        Long familyId = 1L; // TODO: JWT에서 familyId 추출하도록 수정 필요
        return communityService.getPersonalQuestions(receiverId, familyId);
    }


    @GetMapping("/api/community/home/public")
    public Map<String,Object> getPublicCommunity() {
        Long familyId = 1L; // TODO: JWT에서 familyId 추출하도록 수정 필요
        return communityService.getPublicQuestions(familyId);
    }

    @GetMapping("/api/community/question/my")
    public List<PersonalQuestionDto> getMyQuestions(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = JwtUtil.validateAndGetUserId(token);
        Long familyId = 1L; // TODO: JWT에서 familyId 추출하도록 수정 필요
        return communityService.getMyQuestions(userId, familyId);
    }

    @PostMapping("/api/community/reply")
    public ResponseEntity<Map<String, Object>> createReply(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CommentRequestDto request) {

        String token = authHeader.replace("Bearer ", "");
        String userIdStr = jwtUtil.validateAndGetUserId(token);
        Long userId = Long.valueOf(userIdStr);
        Long familyId = 1L; // TODO: JWT에서 familyId 추출하도록 수정 필요

        CommentResponseDto commentResponse = communityService.saveComment(userId, request, familyId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("replyId", commentResponse.getReplyId());
        
        // 꽃 정보가 있으면 응답에 추가
        if (commentResponse.getFlower() != null) {
            response.put("flower", commentResponse.getFlower());
            response.put("isNewFlowerUnlocked", commentResponse.getIsNewFlowerUnlocked());
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/api/community/like")
    public ResponseEntity<?> like(@RequestBody LikeRequestDto likeRequestDto) {
        communityService.addLike(likeRequestDto.getType(), likeRequestDto.getId());
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/api/community/question/create")
    public ResponseEntity<Map<String, Object>> createQuestion(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody QuestionCreateRequestDto request) {

        String token = authHeader.replace("Bearer ", "");
        String userIdStr = JwtUtil.validateAndGetUserId(token);
        Long senderId = Long.valueOf(userIdStr);
        Long familyId = 1L; // TODO: JWT에서 familyId 추출하도록 수정

        Long questionId = communityService.createQuestion(senderId, request, familyId);

        Map<String, Object> response = new HashMap<>();
        response.put("questionId", questionId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/community/question/detail/{id}")
    public ResponseEntity<QuestionDetailResponseDto> getQuestionDetail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        String token = authHeader.replace("Bearer ", "");
        JwtUtil.validateAndGetUserId(token); // 토큰 검증

        QuestionDetailResponseDto response = communityService.getQuestionDetail(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/community/question/Pupdate")
    public ResponseEntity<Map<String, String>> updatePublicQuestions() {
        Long familyId = 1L; // TODO: JWT에서 familyId 추출하도록 수정
        communityService.updatePublicQuestion(familyId);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/community/question/public")
    public ResponseEntity<PublicQuestionResponseDto> getLatestPublicQuestion() {
        Long familyId = 1L; // TODO: JWT에서 familyId 추출하도록 수정
        PublicQuestionResponseDto response = communityService.getLatestPublicQuestion(familyId);
        
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/community/question/detail/public/{id}")
    public ResponseEntity<QuestionDetailResponseDto> getPublicQuestionDetail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        String token = authHeader.replace("Bearer ", "");
        JwtUtil.validateAndGetUserId(token); // 토큰 검증

        QuestionDetailResponseDto response = communityService.getPublicQuestionDetail(id);
        return ResponseEntity.ok(response);
    }

}