package org.dcode.artificialswbackend.book;

import org.dcode.artificialswbackend.book.dto.FlowerBookResponseDto;
import org.dcode.artificialswbackend.community.entity.FlowerCatalog;
import org.dcode.artificialswbackend.community.entity.Flowers;
import org.dcode.artificialswbackend.community.repository.FlowerCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final FlowerCatalogRepository flowerCatalogRepository;

    public BookService(FlowerCatalogRepository flowerCatalogRepository) {
        this.flowerCatalogRepository = flowerCatalogRepository;
    }

    public List<FlowerBookResponseDto> getFlowerBook(Long familyId) {
        // 해당 가족의 꽃 도감 조회
        List<FlowerCatalog> familyCatalog = flowerCatalogRepository.findByFamilyIdOrderByFlowerType(familyId);
        
        // Map으로 변환 (빠른 조회를 위해)
        Map<Flowers.FlowerType, FlowerCatalog> catalogMap = familyCatalog.stream()
                .collect(Collectors.toMap(FlowerCatalog::getFlowerType, catalog -> catalog));
        
        // 모든 꽃 타입에 대해 응답 생성
        return Arrays.stream(Flowers.FlowerType.values())
                .map(flowerType -> {
                    FlowerCatalog catalog = catalogMap.get(flowerType);
                    
                    if (catalog != null) {
                        // 도감에 있는 경우 - 실제 unlock 상태 반환
                        return new FlowerBookResponseDto(
                            flowerType.getKoreanName(),
                            flowerType.getValue(),
                            catalog.getUnlocked(),
                            catalog.getUnlockedAt()
                        );
                    } else {
                        // 도감에 없는 경우 - unlock되지 않은 상태로 반환
                        return new FlowerBookResponseDto(
                            flowerType.getKoreanName(),
                            flowerType.getValue(),
                            false,
                            null
                        );
                    }
                })
                .collect(Collectors.toList());
    }
}