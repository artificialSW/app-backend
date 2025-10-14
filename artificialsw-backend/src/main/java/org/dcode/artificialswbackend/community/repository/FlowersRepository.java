package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.Flowers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowersRepository extends JpaRepository<Flowers, Long> {
    
    // 특정 트리의 모든 꽃들 조회 (생성 시간 순으로 정렬)
    List<Flowers> findByTreeIdOrderByCreatedAtDesc(Long treeId);
}