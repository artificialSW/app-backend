package org.dcode.artificialswbackend.puzzle.repository;

import org.dcode.artificialswbackend.puzzle.entity.FruitCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FruitCatalogRepository extends JpaRepository<FruitCatalog, Integer> {
    @Query(value = "SELECT * FROM fruit_catalog ORDER BY RAND() LIMIT 1", nativeQuery = true)
    FruitCatalog findRandomFruit();
}
