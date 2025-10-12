package org.dcode.artificialswbackend.archive.repository;

import org.dcode.artificialswbackend.archive.entity.IslandArchives;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IslandArchivesRepository extends JpaRepository<IslandArchives, Long> {
    Optional<IslandArchives> findByFamilyIdAndYearAndMonthAndPeriod(Long familyId, Integer year, Integer month, Integer period);
    Optional<IslandArchives> findByIdAndFamilyId(Long id, Long familyId);

}
