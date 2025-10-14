package org.dcode.artificialswbackend.community.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dcode.artificialswbackend.community.dto.AiPredictionResponseDto;
import org.dcode.artificialswbackend.community.dto.FlowerResultDto;
import org.dcode.artificialswbackend.community.entity.Flowers;
import org.dcode.artificialswbackend.community.entity.FlowerCatalog;
import org.dcode.artificialswbackend.archive.entity.IslandArchives;
import org.dcode.artificialswbackend.archive.entity.Tree;
import org.dcode.artificialswbackend.community.repository.FlowersRepository;
import org.dcode.artificialswbackend.community.repository.FlowerCatalogRepository;
import org.dcode.artificialswbackend.archive.repository.IslandArchivesRepository;
import org.dcode.artificialswbackend.archive.repository.TreeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class FlowerService {
    
    private final FlowersRepository flowersRepository;
    private final TreeRepository treeRepository;
    private final IslandArchivesRepository islandArchivesRepository;
    private final FlowerCatalogRepository flowerCatalogRepository;
    private final ObjectMapper objectMapper;
    
    public FlowerService(FlowersRepository flowersRepository, 
                        TreeRepository treeRepository,
                        IslandArchivesRepository islandArchivesRepository,
                        FlowerCatalogRepository flowerCatalogRepository) {
        this.flowersRepository = flowersRepository;
        this.treeRepository = treeRepository;
        this.islandArchivesRepository = islandArchivesRepository;
        this.flowerCatalogRepository = flowerCatalogRepository;
        this.objectMapper = new ObjectMapper();
    }
    
    public FlowerResultDto processAiResponseAndSaveFlower(String aiResponse, Long questionRefId, Long familyId) {
        try {
            // 1. AI 응답 파싱
            AiPredictionResponseDto aiResult = objectMapper.readValue(aiResponse, AiPredictionResponseDto.class);
            
            // 2. 현재 한국 날짜 기준으로 period와 position 결정
            LocalDate koreaDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
            int year = koreaDate.getYear();
            int month = koreaDate.getMonthValue();
            int day = koreaDate.getDayOfMonth();
            
            // 3. 날짜에 따른 period와 position 결정
            int period;
            int position;
            
            if (day <= 15) {
                // 1-15일: period=1
                period = 1;
                if (day <= 7) {
                    position = 1; // 첫 7일은 position=1
                } else {
                    position = 2; // 8-15일은 position=2
                }
            } else {
                // 16-말일: period=2
                period = 2;
                int dayInPeriod = day - 15; // 16일부터를 1일로 계산
                if (dayInPeriod <= 7) {
                    position = 1; // 첫 7일(16-22일)은 position=1
                } else {
                    position = 2; // 나머지 날은 position=2
                }
            }
            
            // 4. 해당 가족의 period에 맞는 아카이브 찾기 (이미 생성되어 있다고 가정)
            Optional<IslandArchives> archiveOpt = islandArchivesRepository.findByFamilyIdAndYearAndMonthAndPeriod(familyId, year, month, period);
            
            if (archiveOpt.isEmpty()) {
                throw new RuntimeException("Archive not found for familyId: " + familyId + ", year: " + year + ", month: " + month + ", period: " + period);
            }
            
            IslandArchives archive = archiveOpt.get();
            
            // 5. position에 맞는 flower tree 찾기 (이미 생성되어 있다고 가정)
            Optional<Tree> treeOpt = treeRepository.findByArchiveIdAndFamilyIdAndPositionAndTreeCategory(
                archive.getId(), familyId, position, Tree.TreeCategory.flower);
            
            if (treeOpt.isEmpty()) {
                throw new RuntimeException("Tree not found for archiveId: " + archive.getId() + ", position: " + position);
            }
            
            Tree selectedTree = treeOpt.get();
            
            // 6. 꽃 정보 저장
            Flowers.FlowerType flowerType = Flowers.FlowerType.fromKoreanName(aiResult.getFlower());
            Flowers flower = new Flowers(selectedTree.getId(), questionRefId, flowerType);
            flowersRepository.save(flower);
            
            // 7. Community Score 증가 (최대 10점까지)
            incrementCommunityScore(archive.getId());
            
            // 8. 도감 unlock 처리
            boolean isNewlyUnlocked = checkAndUnlockFlower(familyId, flowerType);
            
            return new FlowerResultDto(aiResult.getFlower(), isNewlyUnlocked);
            
        } catch (Exception e) {
            System.err.println("Error processing AI response: " + e.getMessage());
            return null;
        }
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
    
    @Transactional
    private void incrementCommunityScore(Long archiveId) {
        try {
            int updatedRows = islandArchivesRepository.incrementCommunityScore(archiveId);
            if (updatedRows > 0) {
                System.out.println("🏆 Community score increased for archive ID: " + archiveId);
            }
        } catch (Exception e) {
            System.err.println("Error incrementing community score: " + e.getMessage());
        }
    }
}