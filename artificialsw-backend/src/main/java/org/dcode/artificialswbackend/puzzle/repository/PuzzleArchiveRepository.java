package org.dcode.artificialswbackend.puzzle.repository;

import org.dcode.artificialswbackend.puzzle.entity.PuzzleArchive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuzzleArchiveRepository extends JpaRepository<PuzzleArchive, Long> {
    List<PuzzleArchive> findByFamiliesId(Long familiesId);
}
