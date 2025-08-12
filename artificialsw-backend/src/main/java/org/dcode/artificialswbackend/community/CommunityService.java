package org.dcode.artificialswbackend.community;

import org.dcode.artificialswbackend.community.entity.Community;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityService {

    private final List<Community> communities = new ArrayList<>();

    public CommunityService() {
        // 초기 더미 데이터
        communities.add(new Community(1L, "첫 번째 커뮤니티"));
        communities.add(new Community(2L, "두 번째 커뮤니티"));
    }

    public List<Community> findAll() {
        return communities;
    }

    public Community save(Community community) {
        // 새 id 부여
        long newId = communities.isEmpty() ? 1L : communities.get(communities.size() - 1).getId() + 1;
        community.setId(newId);
        communities.add(community);
        return community;
    }
}
