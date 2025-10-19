package org.dcode.artificialswbackend.puzzle.repository;

import org.dcode.artificialswbackend.puzzle.entity.FamilyFruitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FamilyFruitStatusRepository extends JpaRepository<FamilyFruitStatus, Long> {
    Optional<FamilyFruitStatus> findByFamilyIdAndFruitId(Long familyId, Long fruitId);
    List<FamilyFruitStatus> findAllByFamilyIdAndUnlockedTrue(Long familyId);
}
