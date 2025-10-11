package org.dcode.artificialswbackend.archive.repository;

import org.dcode.artificialswbackend.archive.entity.IslandArchives;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IslandArchivesRepository extends JpaRepository<IslandArchives, Long> {
    Optional<IslandArchives> findByFamilyIdAndYearAndMonth(Long familyID, Integer year, Integer month);
}
