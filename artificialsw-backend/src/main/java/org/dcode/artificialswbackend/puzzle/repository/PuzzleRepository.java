package org.dcode.artificialswbackend.puzzle.repository;

import org.dcode.artificialswbackend.puzzle.entity.Puzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PuzzleRepository extends JpaRepository<Puzzle, Integer> {
    @Query(value = "SELECT * FROM puzzle WHERE be_puzzle = 0 AND families_id = :familyId ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Puzzle findRandomBePuzzleZeroByFamilyId(Long familyId);

    List<Puzzle> findByFamiliesIdAndCompletedAndBePuzzle(Long familiesId, boolean completed, Integer bePuzzle);

    List<Puzzle> findByFamiliesIdAndCompleted(Long familiesId, boolean completed);

    List<Puzzle> findByFamiliesIdAndCompletedAndCategoryIdIn(Long familyId, boolean b, List<Long> categoryIds);

    int countByFamiliesIdAndUploaderIdAndCategoryIdIn(Long familyId, Integer userId, List<Long> categoryIds);

    int countByFamiliesIdAndCompletedAndBePuzzle(Long familyId, boolean b, int i);

    int countByFamiliesIdAndBePuzzle(Long familyId, int i);
    
    // 사용자가 contributor로 참여하면서 완성된 퍼즐들 조회
    @Query(value = "SELECT * FROM puzzle WHERE families_id = :familyId AND completed = 1 AND (JSON_CONTAINS(contributors, CAST(:userId AS JSON)) = 1 OR solver_id = :userId) ORDER BY completed_time DESC", nativeQuery = true)
    List<Puzzle> findCompletedPuzzlesByUserContribution(@Param("familyId") Long familyId, @Param("userId") Long userId);
}

