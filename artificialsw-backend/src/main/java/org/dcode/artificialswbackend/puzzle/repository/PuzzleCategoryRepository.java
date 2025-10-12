package org.dcode.artificialswbackend.puzzle.repository;

import org.dcode.artificialswbackend.puzzle.entity.PuzzleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuzzleCategoryRepository extends JpaRepository<PuzzleCategory, Long> {
    PuzzleCategory findByCategory(String category);
}
