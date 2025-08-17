package org.dcode.artificialswbackend.community;

import org.dcode.artificialswbackend.community.entity.Community;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommunityController {

    private final CommunityRepository communityRepository;
    public CommunityController(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    @GetMapping("/api/community/home")
    public List<Community> getHomeCommunity() {
        return communityRepository.findAll();
    }
}