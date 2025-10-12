package org.dcode.artificialswbackend.puzzle.repository;

import org.dcode.artificialswbackend.puzzle.entity.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PuzzleRepository extends JpaRepository<Puzzle, Integer> {
    @Query(value = "SELECT * FROM puzzle WHERE be_puzzle = 0 AND families_id = :familyId ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Puzzle findRandomBePuzzleZeroByFamilyId(Long familyId);

    List<Puzzle> findByFamiliesIdAndCompletedAndBePuzzle(Long familiesId, boolean completed, Integer bePuzzle);

    List<Puzzle> findByFamiliesIdAndCompleted(Long familiesId, boolean completed);

    List<Puzzle> findByFamiliesIdAndCompletedAndCategoryIdIn(Long familyId, boolean b, List<Long> categoryIds);

    int countByFamiliesIdAndUploaderIdAndCategoryIdIn(Long familyId, Integer userId, List<Long> categoryIds);

    int countByFamiliesIdAndCompletedAndBePuzzle(Long familyId, boolean b, int i);
}

