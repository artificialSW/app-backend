package org.dcode.artificialswbackend.puzzle.repository;

import org.dcode.artificialswbackend.puzzle.entity.FruitCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FruitCatalogRepository extends JpaRepository<FruitCatalog, Integer> {
    @Query(value = "SELECT * FROM fruit_catalog ORDER BY RAND() LIMIT 1", nativeQuery = true)
    FruitCatalog findRandomFruit();

    @Query(value = "SELECT fc.* FROM fruit_catalog fc JOIN family_fruit_status s ON fc.id = s.fruit_id WHERE s.family_id = :familyId AND fc.season = :season AND s.unlocked = true", nativeQuery = true)
    List<FruitCatalog> findUnlockedFruitsByFamilyAndSeason(@Param("familyId") Long familyId, @Param("season") String season);

    List<FruitCatalog> findBySeason(String season);
}
