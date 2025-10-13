package org.dcode.artificialswbackend.community;
import org.dcode.artificialswbackend.community.dto.CommentRequestDto;
import org.dcode.artificialswbackend.community.dto.LikeRequestDto;
import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
import org.dcode.artificialswbackend.community.dto.QuestionCreateRequestDto;
import org.dcode.artificialswbackend.community.dto.QuestionDetailResponseDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionResponseDto;
import org.dcode.artificialswbackend.community.dto.CommentResponseDto;
import org.dcode.artificialswbackend.community.dto.FamilyMembersResponseDto;
import org.dcode.artificialswbackend.community.dto.QuestionWithCommentsResponseDto;
import org.dcode.artificialswbackend.community.dto.PublicQuestionWithCommentsResponseDto;
import org.dcode.artificialswbackend.community.dto.MyQuestionsResponseDto;
import org.dcode.artificialswbackend.community.dto.LikeResponseDto;
import org.dcode.artificialswbackend.util.JwtUtil;
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
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        return communityService.getPersonalQuestions(receiverId, familyId);
    }


    @GetMapping("/api/community/home/public")
    public Map<String,Object> getPublicCommunity(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        return communityService.getPublicQuestions(familyId);
    }

    @GetMapping("/api/community/question/my")
    public ResponseEntity<MyQuestionsResponseDto> getMyQuestions(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.validateAndGetUserId(token);
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        MyQuestionsResponseDto response = communityService.getMyQuestions(userId, familyId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/community/reply")
    public ResponseEntity<CommentResponseDto> createReply(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CommentRequestDto request) {

        String token = authHeader.replace("Bearer ", "");
        String userIdStr = jwtUtil.validateAndGetUserId(token);
        Long userId = Long.valueOf(userIdStr);
        Long familyId = jwtUtil.validateAndGetFamilyId(token);

        CommentResponseDto commentResponse = communityService.saveComment(userId, request, familyId);

        return ResponseEntity.ok(commentResponse);
    }


    @PostMapping("/api/community/like")
    public ResponseEntity<LikeResponseDto> like(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LikeRequestDto likeRequestDto) {
        
        String token = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        
        try {
            LikeResponseDto response = communityService.toggleLike(
                likeRequestDto.getType(), 
                likeRequestDto.getId(), 
                userId, 
                familyId
            );
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                .body(new LikeResponseDto(false, 0, false));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new LikeResponseDto(false, 0, false));
        }
    }

    @PostMapping("/api/community/question/create")
    public ResponseEntity<Map<String, Object>> createQuestion(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody QuestionCreateRequestDto request) {

        String token = authHeader.replace("Bearer ", "");
        String userIdStr = jwtUtil.validateAndGetUserId(token);
        Long senderId = Long.valueOf(userIdStr);
        Long familyId = jwtUtil.validateAndGetFamilyId(token);

        Long questionRefId = communityService.createQuestion(senderId, request, familyId);

        Map<String, Object> response = new HashMap<>();
        response.put("question_ref_id", questionRefId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/community/question/detail/{id}")
    public ResponseEntity<QuestionWithCommentsResponseDto> getQuestionDetail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        String token = authHeader.replace("Bearer ", "");
        jwtUtil.validateAndGetUserId(token); // 토큰 검증

        QuestionWithCommentsResponseDto response = communityService.getQuestionDetail(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/community/question/Pupdate")
    public ResponseEntity<Map<String, String>> updatePublicQuestions(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        communityService.updatePublicQuestion(familyId);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/community/home/thisweek")
    public ResponseEntity<Map<String, Object>> getThisWeekQuestion(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.validateAndGetUserId(token);
        Long receiverId = Long.valueOf(userId);
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        
        Map<String, Object> response = communityService.getThisWeekQuestionWithComments(receiverId, familyId);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/community/question/detail/public/{id}")
    public ResponseEntity<PublicQuestionWithCommentsResponseDto> getPublicQuestionDetail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        String token = authHeader.replace("Bearer ", "");
        jwtUtil.validateAndGetUserId(token); // 토큰 검증

        PublicQuestionWithCommentsResponseDto response = communityService.getPublicQuestionDetail(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/community/question/family")
    public ResponseEntity<FamilyMembersResponseDto> getFamilyMembers(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        
        FamilyMembersResponseDto response = communityService.getFamilyMembers(familyId);
        return ResponseEntity.ok(response);
    }

}