package org.dcode.artificialswbackend.puzzle.repository;

import org.dcode.artificialswbackend.puzzle.entity.PuzzlePieces;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PuzzlePiecesRepository extends JpaRepository<PuzzlePieces, Long> {
    Optional<PuzzlePieces> findByPuzzleId(Integer puzzleId);
}
