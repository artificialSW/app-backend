package org.dcode.artificialswbackend.puzzle.repository;

import org.dcode.artificialswbackend.puzzle.entity.PuzzleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuzzleCategoryRepository extends JpaRepository<PuzzleCategory, Long> {
    PuzzleCategory findByCategory(String category);
    List<PuzzleCategory> findByIdBetween(Long start, Long end);

    List<PuzzleCategory> findByCategoryIn(List<String> activeCategoryNames);
}

