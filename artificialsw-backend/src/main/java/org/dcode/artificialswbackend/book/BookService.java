package org.dcode.artificialswbackend.book;

import org.dcode.artificialswbackend.book.dto.FlowerBookResponseDto;
import org.dcode.artificialswbackend.community.entity.FlowerCatalog;
import org.dcode.artificialswbackend.community.repository.FlowerCatalogRepository;
import org.dcode.artificialswbackend.puzzle.entity.FamilyFruitStatus;
import org.dcode.artificialswbackend.puzzle.repository.FamilyFruitStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final FlowerCatalogRepository flowerCatalogRepository;
    private final FamilyFruitStatusRepository familyFruitStatusRepository;

    public BookService(FlowerCatalogRepository flowerCatalogRepository, FamilyFruitStatusRepository familyFruitStatusRepository) {
        this.flowerCatalogRepository = flowerCatalogRepository;
        this.familyFruitStatusRepository = familyFruitStatusRepository;
    }

    public FlowerBookResponseDto getFlowerBook(Long familyId) {
        // 해당 가족의 해금된 꽃 도감 조회
        List<FlowerCatalog> familyCatalog = flowerCatalogRepository.findByFamilyIdOrderByFlowerType(familyId);
        
        // 해금된 꽃들의 번호만 추출
        List<Integer> resolvedFlowers = familyCatalog.stream()
                .filter(FlowerCatalog::getUnlocked) // unlocked가 true인 것만
                .map(catalog -> catalog.getFlowerType().getFlowerNumber()) // 꽃 번호로 변환
                .collect(Collectors.toList());
        
        return new FlowerBookResponseDto(resolvedFlowers);
    }

    public List<Integer> getResolvedFruits(Long familyId) {
        return familyFruitStatusRepository.findAllByFamilyIdAndUnlockedTrue(familyId)
                .stream()
                .map(status -> status.getFruitId().intValue())
                .collect(Collectors.toList());
    }
}