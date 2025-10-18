package org.dcode.artificialswbackend.puzzle.repository;

import org.dcode.artificialswbackend.puzzle.entity.Fruits;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FruitsRepository extends JpaRepository<Fruits, Long> {
    List<Fruits> findByTreeId(Long treeId);
    List<Fruits> findByPuzzleId(Integer puzzleId);

    List<Fruits> findAllByTreeId(Long id);
}
