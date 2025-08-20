package org.dcode.artificialswbackend.community;

import org.springframework.web.bind.annotation.*;

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
}