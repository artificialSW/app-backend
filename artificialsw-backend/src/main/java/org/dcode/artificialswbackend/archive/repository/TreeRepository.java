package org.dcode.artificialswbackend.archive.repository;

import org.dcode.artificialswbackend.archive.entity.Tree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TreeRepository extends JpaRepository<Tree, Long> {
    List<Tree> findByArchiveId(Long archiveId);
    Optional<Tree> findByArchiveIdAndFamilyIdAndPositionAndTreeCategory(
            Long archiveId, Long familyId, Integer position, Tree.TreeCategory treeCategory
    );
}
