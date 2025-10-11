package org.dcode.artificialswbackend.archive;

import jakarta.transaction.Transactional;
import org.dcode.artificialswbackend.archive.entity.IslandArchives;
import org.dcode.artificialswbackend.archive.entity.Tree;
import org.dcode.artificialswbackend.archive.repository.IslandArchivesRepository;
import org.dcode.artificialswbackend.archive.repository.TreeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ArchiveService {
    private final IslandArchivesRepository islandArchivesRepository;
    private final TreeRepository treeRepository;

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
            tree.setTreeCategory(position <= 2 ? Tree.TreeCategory.FLOWER : Tree.TreeCategory.FRUIT);
            treeRepository.save(tree);
        }
    }
}