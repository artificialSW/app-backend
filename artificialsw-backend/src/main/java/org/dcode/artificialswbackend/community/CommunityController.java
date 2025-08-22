package org.dcode.artificialswbackend.community;

import org.dcode.artificialswbackend.community.dto.PersonalQuestionDto;
import org.dcode.artificialswbackend.community.util.JwtUtil;
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
}