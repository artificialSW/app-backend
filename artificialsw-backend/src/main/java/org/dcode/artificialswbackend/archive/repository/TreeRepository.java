package org.dcode.artificialswbackend.archive.repository;

import org.dcode.artificialswbackend.archive.entity.Tree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreeRepository extends JpaRepository<Tree, Long> {
    List<Tree> findByArchiveId(Long archiveId);
}
