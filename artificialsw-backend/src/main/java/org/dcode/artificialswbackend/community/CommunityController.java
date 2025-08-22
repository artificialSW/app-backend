package org.dcode.artificialswbackend.community;
import org.dcode.artificialswbackend.community.dto.CommentRequestDto;
import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
import org.dcode.artificialswbackend.community.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping("/api/community/home/{receiverId}")
    public Map<String,Object> getHomeCommunity(@PathVariable Long receiverId) {
        return communityService.getQuestionsWithUnsolvedCount(receiverId);
    }

    @GetMapping("/api/community/home/public")
    public Map<String,Object> getPublicCommunity() {
        return communityService.getPublicQuestions();
    }

    @GetMapping("/api/community/question/my")
    public List<PersonalQuestionDto> getMyQuestions(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = JwtUtil.validateAndGetUserId(token);
        return communityService.getMyQuestions(userId);
    }

    @PostMapping("/api/community/reply")
    public ResponseEntity<?> createReply(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CommentRequestDto request) {
        String token = authHeader.replace("Bearer ", "");
        String userIdStr = JwtUtil.validateAndGetUserId(token);
        Long userId = Long.valueOf(userIdStr);

        Long replyId = communityService.saveComment(userId, request);

        return ResponseEntity.ok(Map.of("success", true, "replyId", replyId));
    }

}