package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Long> {
    
    @Query("SELECT t FROM Tree t WHERE t.archiveId = :archiveId AND t.treeCategory = 'FLOWER' ORDER BY t.id ASC")
    List<Tree> findFlowerTreesByArchiveIdOrderById(@Param("archiveId") Long archiveId);
}