package org.dcode.artificialswbackend.tree;

import org.dcode.artificialswbackend.tree.dto.TreeFlowerResponseDto;
import org.dcode.artificialswbackend.community.entity.Flowers;
import org.dcode.artificialswbackend.community.entity.QuestionReference;
import org.dcode.artificialswbackend.community.repository.FlowersRepository;
import org.dcode.artificialswbackend.community.repository.QuestionReferenceRepository;
import org.dcode.artificialswbackend.archive.repository.IslandArchivesRepository;
import org.dcode.artificialswbackend.archive.repository.TreeRepository;
import org.dcode.artificialswbackend.archive.entity.IslandArchives;
import org.dcode.artificialswbackend.archive.entity.Tree;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TreeService {

    private final FlowersRepository flowersRepository;
    private final IslandArchivesRepository islandArchivesRepository;
    private final TreeRepository treeRepository;
    private final QuestionReferenceRepository questionReferenceRepository;

    public TreeService(FlowersRepository flowersRepository, 
                      IslandArchivesRepository islandArchivesRepository, 
                      TreeRepository treeRepository,
                      QuestionReferenceRepository questionReferenceRepository) {
        this.flowersRepository = flowersRepository;
        this.islandArchivesRepository = islandArchivesRepository;
        this.treeRepository = treeRepository;
        this.questionReferenceRepository = questionReferenceRepository;
    }

    public List<TreeFlowerResponseDto> getTreeFlowers(Long treeId) {
        // treeId로 꽃들 조회
        List<Flowers> flowers = flowersRepository.findByTreeIdOrderByCreatedAtDesc(treeId);
        
        // DTO로 변환하면서 영어 이름 적용
        return flowers.stream()
                .map(flower -> new TreeFlowerResponseDto(
                    flower.getId(),
                    flower.getFlower().getEnglishName(),
                    flower.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<TreeFlowerResponseDto> getTreeFlowersByParams(Long familyId, int year, int month, int period, int position) {
        // 1. year, month, period로 IslandArchives 찾기
        Optional<IslandArchives> archiveOpt = islandArchivesRepository.findByFamilyIdAndYearAndMonthAndPeriod(familyId, year, month, period);
        
        if (archiveOpt.isEmpty()) {
            throw new RuntimeException("Archive not found for familyId: " + familyId + ", year: " + year + ", month: " + month + ", period: " + period);
        }
        
        IslandArchives archive = archiveOpt.get();
        
        // 2. archiveId, familyId, position으로 Tree 찾기 (flower 카테고리)
        Optional<Tree> treeOpt = treeRepository.findByArchiveIdAndFamilyIdAndPositionAndTreeCategory(
            archive.getId(), familyId, position, Tree.TreeCategory.flower);
        
        if (treeOpt.isEmpty()) {
            throw new RuntimeException("Flower tree not found for archiveId: " + archive.getId() + 
                                     ", familyId: " + familyId + ", position: " + position);
        }
        
        Tree tree = treeOpt.get();
        
        // 3. 기존 getTreeFlowers 로직 재사용
        return getTreeFlowers(tree.getId());
    }

    public QuestionReference getFlowerQuestionReference(Long flowerId) {
        // 1. 꽃 ID로 Flowers 엔터티 조회
        Optional<Flowers> flowerOpt = flowersRepository.findById(flowerId);
        if (flowerOpt.isEmpty()) {
            throw new RuntimeException("Flower not found with id: " + flowerId);
        }
        
        Flowers flower = flowerOpt.get();
        Long questionRefId = flower.getQuestionRefId();
        
        if (questionRefId == null) {
            throw new RuntimeException("No question reference found for flower id: " + flowerId);
        }
        
        // 2. QuestionReference 조회
        Optional<QuestionReference> questionRefOpt = questionReferenceRepository.findById(questionRefId);
        if (questionRefOpt.isEmpty()) {
            throw new RuntimeException("Question reference not found with id: " + questionRefId);
        }
        
        return questionRefOpt.get();
    }
}