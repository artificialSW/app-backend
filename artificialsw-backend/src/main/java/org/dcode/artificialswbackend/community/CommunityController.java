package org.dcode.artificialswbackend.community;

import org.dcode.artificialswbackend.community.entity.Community;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    // 모든 커뮤니티 목록 조회
    @GetMapping
    public List<Community> getAllCommunities() {
        return communityService.findAll();
    }

    // 새 커뮤니티 추가
    @PostMapping
    public Community addCommunity(@RequestBody Community community) {
        return communityService.save(community);
    }
}
