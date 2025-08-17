package org.dcode.artificialswbackend.community;

import org.dcode.artificialswbackend.community.entity.Community;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityService {

    private final List<Community> communities = new ArrayList<>();

    public CommunityService() {
    }

    public List<Community> findAll() {
        return communities;
    }

    public Community save(Community community) {
    }
}
