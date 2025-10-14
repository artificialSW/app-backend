package org.dcode.artificialswbackend.archive.repository;

import org.dcode.artificialswbackend.archive.entity.IslandArchives;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IslandArchivesRepository extends JpaRepository<IslandArchives, Long> {
    Optional<IslandArchives> findByFamilyIdAndYearAndMonthAndPeriod(Long familyId, Integer year, Integer month, Integer period);
    Optional<IslandArchives> findByIdAndFamilyId(Long id, Long familyId);
    
    @Modifying
    @Query("UPDATE IslandArchives i SET i.communityScore = CASE WHEN i.communityScore < 10 THEN i.communityScore + 1 ELSE 10 END WHERE i.id = :archiveId")
    int incrementCommunityScore(@Param("archiveId") Long archiveId);
}
