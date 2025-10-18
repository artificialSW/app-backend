package org.dcode.artificialswbackend.archive;

import jakarta.transaction.Transactional;
import org.dcode.artificialswbackend.archive.dto.ArchiveFruitResponse;
import org.dcode.artificialswbackend.archive.dto.FruitResponse;
import org.dcode.artificialswbackend.archive.entity.IslandArchives;
import org.dcode.artificialswbackend.archive.entity.Tree;
import org.dcode.artificialswbackend.archive.repository.IslandArchivesRepository;
import org.dcode.artificialswbackend.archive.repository.TreeRepository;
import org.dcode.artificialswbackend.puzzle.entity.Fruits;
import org.dcode.artificialswbackend.puzzle.entity.Puzzle;
import org.dcode.artificialswbackend.puzzle.repository.FruitsRepository;
import org.dcode.artificialswbackend.puzzle.repository.PuzzleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArchiveService {
    private final IslandArchivesRepository islandArchivesRepository;
    private final TreeRepository treeRepository;
    private final FruitsRepository fruitsRepository;

    @Value("${puzzle.image.url.base}")
    private String imageBaseUrl;


    public ArchiveService(IslandArchivesRepository islandArchivesRepository, TreeRepository treeRepository, FruitsRepository fruitsRepository) {
        this.islandArchivesRepository = islandArchivesRepository;
        this.treeRepository = treeRepository;
        this.fruitsRepository = fruitsRepository;
    }

    @Transactional
    public void ensureIslandAndTrees(Long familyId) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int period = (day <= 15) ? 1 : 2;

        // period까지 포함해서 중복 체크
        if (islandArchivesRepository.findByFamilyIdAndYearAndMonthAndPeriod(familyId, year, month, period).isPresent()) {
            return;
        }

        IslandArchives island = new IslandArchives();
        island.setFamilyId(familyId);
        island.setYear(year);
        island.setMonth(month);
        island.setPeriod(period);
        island = islandArchivesRepository.save(island);

        for (int position = 1; position <= 4; position++) {
            Tree tree = new Tree();
            tree.setArchiveId(island.getId());
            tree.setFamilyId(familyId);
            tree.setPosition(position);
            tree.setTreeCategory(position <= 2 ? Tree.TreeCategory.flower : Tree.TreeCategory.fruit);
            treeRepository.save(tree);
        }
    }
    @Transactional
    public String saveCaptureImage(Long archiveId, Long familyId, String imageBase64) {
        String uploadDir = "/home/ubuntu/app/images/capture/";
        String fileName = System.currentTimeMillis() + ".png";
        byte[] decodedBytes = Base64.getDecoder().decode(imageBase64);
        try {
            Files.write(Paths.get(uploadDir + fileName), decodedBytes);
        } catch (IOException e) {
            throw new RuntimeException("캡쳐 이미지 저장 실패", e);
        }
        String imageUrl = imageBaseUrl + "capture/" + fileName;

        IslandArchives archive = islandArchivesRepository.findByIdAndFamilyId(archiveId, familyId)
                .orElseThrow(() -> new RuntimeException("해당 아카이브를 찾을 수 없습니다."));
        archive.setCaptureImagePath(imageUrl);
        islandArchivesRepository.save(archive);

        return imageUrl;
    }

    public Map<String, Integer> getScores(Long archiveId, Long familyId) {
        IslandArchives archive = islandArchivesRepository.findByIdAndFamilyId(archiveId, familyId)
                .orElseThrow(() -> new RuntimeException("해당 아카이브를 찾을 수 없습니다."));
        return Map.of(
                "puzzle_score", archive.getPuzzleScore(),
                "community_score", archive.getCommunityScore()
        );
    }

    /**
     * API: /api/archives/main/{year}/{month}/{period}/{position}
     * - familyId는 토큰에서 가져와 매개변수로 받음
     * - archiveId: familyId, year, month, period로 조회
     * - treeId: archiveId, position으로 조회
     * - fruits: treeId로 조회, fruitId와 archivedAt 가져옴
     */
    public List<ArchiveFruitResponse> getArchiveFruits(Long familyId, int year, int month, int period, int position) {
        // 1. archiveId 찾기
        IslandArchives archive = islandArchivesRepository.findByFamilyIdAndYearAndMonthAndPeriod(
                familyId, year, month, period
        ).orElseThrow(() -> new RuntimeException("해당 아카이브를 찾을 수 없습니다."));

        // 2. treeId 찾기
        Tree tree = treeRepository.findByArchiveIdAndPosition(
                archive.getId(), position
        ).orElseThrow(() -> new RuntimeException("해당 트리를 찾을 수 없습니다."));

        // 3. fruit 리스트 조회
        List<Fruits> fruitsList = fruitsRepository.findAllByTreeId(tree.getId());

        // 4. DTO 변환
        return fruitsList.stream()
                .map(fruit -> new ArchiveFruitResponse(
                        fruit.getId(),
                        fruit.getFruitName(),
                        fruit.getCreatedAt()  // fruit 테이블의 archived_at 컬럼 활용
                ))
                .collect(Collectors.toList());
    }

    @Service
    public static class FruitService {
        private final FruitsRepository fruitsRepository;
        private final PuzzleRepository puzzleRepository;

        public FruitService(FruitsRepository fruitsRepository, PuzzleRepository puzzleRepository) {
            this.fruitsRepository = fruitsRepository;
            this.puzzleRepository = puzzleRepository;
        }

        public FruitResponse getFruitDetails(Long fruitId, Long familyId) {
            Fruits fruit = fruitsRepository.findById(fruitId)
                    .orElseThrow(() -> new RuntimeException("과일을 찾을 수 없습니다."));

            Integer puzzleId = fruit.getPuzzleId();
            Puzzle puzzle = puzzleRepository.findById(puzzleId)
                    .orElseThrow(() -> new RuntimeException("퍼즐 정보를 찾을 수 없습니다."));

            return new FruitResponse(
                    puzzle.getImagePath(),
                    fruit.getCategory(),
                    fruit.getMessage()
            );
        }
    }
}
