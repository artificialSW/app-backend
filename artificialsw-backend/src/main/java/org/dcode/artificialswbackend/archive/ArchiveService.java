package org.dcode.artificialswbackend.archive;

import jakarta.transaction.Transactional;
import org.dcode.artificialswbackend.archive.entity.IslandArchives;
import org.dcode.artificialswbackend.archive.entity.Tree;
import org.dcode.artificialswbackend.archive.repository.IslandArchivesRepository;
import org.dcode.artificialswbackend.archive.repository.TreeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

@Service
public class ArchiveService {
    private final IslandArchivesRepository islandArchivesRepository;
    private final TreeRepository treeRepository;

    @Value("${puzzle.image.url.base}")
    private String imageBaseUrl;


    public ArchiveService(IslandArchivesRepository islandArchivesRepository, TreeRepository treeRepository) {
        this.islandArchivesRepository = islandArchivesRepository;
        this.treeRepository = treeRepository;
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
}
