package org.dcode.artificialswbackend.community;

import org.dcode.artificialswbackend.community.entity.Community;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;
    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public List<Community> getAllCommunity() {
        return communityRepository.findAll();
    }
}