package org.dcode.artificialswbackend.community;
import org.dcode.artificialswbackend.community.dto.CommentRequestDto;
import org.dcode.artificialswbackend.community.dto.LikeRequestDto;
import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
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

    @GetMapping("/api/community/home")
    public Map<String,Object> getHomeCommunity(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.validateAndGetUserId(token);
        Long receiverId = Long.valueOf(userId);
        return communityService.getQuestionsWithUnsolvedCount(receiverId);
    }


    @GetMapping("/api/community/home/public")
    public Map<String,Object> getPublicCommunity() {
        return communityService.getPublicQuestions();
    }

    @GetMapping("/api/community/question/my")
    public List<PersonalQuestionDto> getMyQuestions(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.validateAndGetUserId(token);
        return communityService.getMyQuestions(userId);
    }

    @PostMapping("/api/community/reply")
    public ResponseEntity<Map<String, Object>> createReply(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CommentRequestDto request) {

        String token = authHeader.replace("Bearer ", "");
        String userIdStr = jwtUtil.validateAndGetUserId(token);
        Long userId = Long.valueOf(userIdStr);

        Long replyId = communityService.saveComment(userId, request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("replyId", replyId);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/api/community/like")
    public ResponseEntity<?> like(@RequestBody LikeRequestDto likeRequestDto) {
        communityService.addLike(likeRequestDto.getType(), likeRequestDto.getId());
        return ResponseEntity.ok(Map.of("success", true));
    }

}