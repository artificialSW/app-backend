package org.dcode.artificialswbackend.community.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dcode.artificialswbackend.community.dto.AiPredictionResponseDto;
import org.dcode.artificialswbackend.community.dto.FlowerResultDto;
import org.dcode.artificialswbackend.community.entity.Flowers;
import org.dcode.artificialswbackend.community.entity.FlowerCatalog;
import org.dcode.artificialswbackend.community.entity.IslandArchive;
import org.dcode.artificialswbackend.community.entity.Tree;
import org.dcode.artificialswbackend.community.repository.FlowersRepository;
import org.dcode.artificialswbackend.community.repository.FlowerCatalogRepository;
import org.dcode.artificialswbackend.community.repository.IslandArchiveRepository;
import org.dcode.artificialswbackend.community.repository.TreeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class FlowerService {
    
    private final FlowersRepository flowersRepository;
    private final TreeRepository treeRepository;
    private final IslandArchiveRepository islandArchiveRepository;
    private final FlowerCatalogRepository flowerCatalogRepository;
    private final ObjectMapper objectMapper;
    
    public FlowerService(FlowersRepository flowersRepository, 
                        TreeRepository treeRepository,
                        IslandArchiveRepository islandArchiveRepository,
                        FlowerCatalogRepository flowerCatalogRepository) {
        this.flowersRepository = flowersRepository;
        this.treeRepository = treeRepository;
        this.islandArchiveRepository = islandArchiveRepository;
        this.flowerCatalogRepository = flowerCatalogRepository;
        this.objectMapper = new ObjectMapper();
    }
    
    public FlowerResultDto processAiResponseAndSaveFlower(String aiResponse, Long questionRefId, Long familyId) {
        try {
            // 1. AI 응답 파싱
            AiPredictionResponseDto aiResult = objectMapper.readValue(aiResponse, AiPredictionResponseDto.class);
            
            // 2. 현재 한국 날짜 기준으로 아카이브 찾기
            LocalDate koreaDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
            int year = koreaDate.getYear();
            int month = koreaDate.getMonthValue();
            
            // 3. 해당 가족의 이번 달 아카이브 찾기 (없으면 생성)
            Optional<IslandArchive> archiveOpt = islandArchiveRepository.findByFamilyIdAndYearAndMonth(familyId, year, month);
            IslandArchive archive;
            
            if (archiveOpt.isEmpty()) {
                // 아카이브가 없으면 생성
                archive = new IslandArchive(familyId, month, year, 1);
                archive = islandArchiveRepository.save(archive);
                
                // 새 아카이브에 대한 4개의 트리 생성 (2개는 flower, 2개는 fruit)
                createTreesForArchive(archive.getId(), familyId);
            } else {
                archive = archiveOpt.get();
            }
            
            // 4. 날짜에 따른 트리 선택 (1-15일: 첫 번째 flower tree, 16-말일: 두 번째 flower tree)
            List<Tree> flowerTrees = treeRepository.findFlowerTreesByArchiveIdOrderById(archive.getId());
            
            if (flowerTrees.isEmpty()) {
                // flower tree가 없으면 생성
                createTreesForArchive(archive.getId(), familyId);
                flowerTrees = treeRepository.findFlowerTreesByArchiveIdOrderById(archive.getId());
            }
            
            Tree selectedTree;
            if (koreaDate.getDayOfMonth() <= 15) {
                selectedTree = flowerTrees.get(0); // 첫 번째 flower tree
            } else {
                selectedTree = flowerTrees.size() > 1 ? flowerTrees.get(1) : flowerTrees.get(0); // 두 번째 flower tree
            }
            
            // 5. 꽃 정보 저장
            Flowers.FlowerType flowerType = Flowers.FlowerType.fromKoreanName(aiResult.getFlower());
            Flowers flower = new Flowers(selectedTree.getId(), questionRefId, flowerType);
            flowersRepository.save(flower);
            
            // 6. 도감 unlock 처리
            boolean isNewlyUnlocked = checkAndUnlockFlower(familyId, flowerType);
            
            return new FlowerResultDto(aiResult.getFlower(), isNewlyUnlocked);
            
        } catch (Exception e) {
            System.err.println("Error processing AI response: " + e.getMessage());
            return null;
        }
    }
    
    private void createTreesForArchive(Long archiveId, Long familyId) {
        // 2개의 flower tree 생성
        Tree flowerTree1 = new Tree(archiveId, familyId, Tree.TreeCategory.FLOWER);
        Tree flowerTree2 = new Tree(archiveId, familyId, Tree.TreeCategory.FLOWER);
        
        // 2개의 fruit tree 생성
        Tree fruitTree1 = new Tree(archiveId, familyId, Tree.TreeCategory.FRUIT);
        Tree fruitTree2 = new Tree(archiveId, familyId, Tree.TreeCategory.FRUIT);
        
        treeRepository.save(flowerTree1);
        treeRepository.save(flowerTree2);
        treeRepository.save(fruitTree1);
        treeRepository.save(fruitTree2);
    }
    
    private boolean checkAndUnlockFlower(Long familyId, Flowers.FlowerType flowerType) {
        // 해당 가족의 해당 꽃 도감 조회
        Optional<FlowerCatalog> catalogOpt = flowerCatalogRepository.findByFamilyIdAndFlowerType(familyId, flowerType);
        
        if (catalogOpt.isEmpty()) {
            // 도감 엔트리가 없으면 새로 생성하고 unlock
            FlowerCatalog catalog = new FlowerCatalog(familyId, flowerType);
            catalog.unlock();
            flowerCatalogRepository.save(catalog);
            return true; // 새로 unlock됨
        } else {
            FlowerCatalog catalog = catalogOpt.get();
            if (!catalog.getUnlocked()) {
                // 아직 unlock되지 않았다면 unlock
                catalog.unlock();
                flowerCatalogRepository.save(catalog);
                return true; // 새로 unlock됨
            } else {
                return false; // 이미 unlock된 상태
            }
        }
    }
}