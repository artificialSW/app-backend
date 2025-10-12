package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.IslandArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IslandArchiveRepository extends JpaRepository<IslandArchive, Long> {
    
    @Query("SELECT ia FROM IslandArchive ia WHERE ia.familyId = :familyId AND ia.year = :year AND ia.month = :month")
    Optional<IslandArchive> findByFamilyIdAndYearAndMonth(@Param("familyId") Long familyId, 
                                                         @Param("year") Integer year, 
                                                         @Param("month") Integer month);
}