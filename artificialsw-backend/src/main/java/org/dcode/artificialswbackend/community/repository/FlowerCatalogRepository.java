package org.dcode.artificialswbackend.community.repository;

import org.dcode.artificialswbackend.community.entity.FlowerCatalog;
import org.dcode.artificialswbackend.community.entity.Flowers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowerCatalogRepository extends JpaRepository<FlowerCatalog, Long> {
    
    @Query("SELECT fc FROM FlowerCatalog fc WHERE fc.familyId = :familyId AND fc.flowerType = :flowerType")
    Optional<FlowerCatalog> findByFamilyIdAndFlowerType(@Param("familyId") Long familyId, 
                                                        @Param("flowerType") Flowers.FlowerType flowerType);
    
    // 특정 가족의 모든 꽃 도감 조회
    List<FlowerCatalog> findByFamilyIdOrderByFlowerType(Long familyId);
}