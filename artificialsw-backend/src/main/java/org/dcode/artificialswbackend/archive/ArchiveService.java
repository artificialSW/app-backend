package org.dcode.artificialswbackend.archive;

import jakarta.transaction.Transactional;
import org.dcode.artificialswbackend.archive.dto.ArchiveFlowerResponse;
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
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArchiveService {
    private final IslandArchivesRepository islandArchivesRepository;
    private final TreeRepository treeRepository;
    private final FruitsRepository fruitsRepository;
    private final PuzzleRepository puzzleRepository;

    @Value("${puzzle.image.url.base}")
    private String imageBaseUrl;


    public ArchiveService(IslandArchivesRepository islandArchivesRepository, TreeRepository treeRepository, FruitsRepository fruitsRepository, PuzzleRepository puzzleRepository) {
        this.islandArchivesRepository = islandArchivesRepository;
        this.treeRepository = treeRepository;
        this.fruitsRepository = fruitsRepository;
        this.puzzleRepository = puzzleRepository;
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
     * 1. familyId, year, month, period로 IslandArchives (archive) 찾기
     * 2. archiveId와 position으로 Tree 객체 조회
     * 3. treeId로 Fruits 리스트 조회
     * 4. 각 Fruits의 puzzleId로 Puzzle 객체를 찾아 completedTime을 archivedAt으로 사용
     * 5. Fruits 정보와 archivedAt을 DTO 형태로 변환 후 리스트 반환
     */
    public List<ArchiveFlowerResponse> getArchiveFlowers(Long familyId, Integer year, Integer month, Integer period, Integer position) {
        // 1. 아카이브 찾기
        IslandArchives archive = islandArchivesRepository.findByFamilyIdAndYearAndMonthAndPeriod(familyId, year, month, period)
                .orElseThrow(() -> new RuntimeException("해당 아카이브를 찾을 수 없습니다."));

        // 2. 트리 조회 (archiveId + position)
        Tree tree = treeRepository.findByArchiveIdAndPosition(archive.getId(), position)
                .orElseThrow(() -> new RuntimeException("해당 트리를 찾을 수 없습니다."));

        // 3. 과일(또는 꽃) 리스트 조회
        List<Fruits> fruits = fruitsRepository.findAllByTreeId(tree.getId());

        // 4. Fruits -> DTO 변환
        List<ArchiveFlowerResponse> responseDtos = fruits.stream().map(fruit -> {
            LocalDateTime archivedAt = null;

            // 5. puzzleId가 있으면 puzzle에서 완료 시간 가져오기
            if (fruit.getPuzzleId() != null) {
                Puzzle puzzle = puzzleRepository.findById(fruit.getPuzzleId()).orElse(null);
                if (puzzle != null) {
                    archivedAt = puzzle.getCompletedTime();
                }
            }

            // 6. DTO 생성 및 반환
            return new ArchiveFlowerResponse(fruit.getId(), fruit.getFruitName(), archivedAt);
        }).collect(Collectors.toList());

        return responseDtos;
    }

}
