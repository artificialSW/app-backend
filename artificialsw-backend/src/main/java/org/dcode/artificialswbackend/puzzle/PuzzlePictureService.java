package org.dcode.artificialswbackend.puzzle;

import org.dcode.artificialswbackend.puzzle.dto.PictureData;
import org.dcode.artificialswbackend.puzzle.entity.Puzzle;
import org.dcode.artificialswbackend.puzzle.entity.PuzzleCategory;
import org.dcode.artificialswbackend.puzzle.repository.PuzzleCategoryRepository;
import org.dcode.artificialswbackend.puzzle.repository.PuzzleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class PuzzlePictureService {
    private final PuzzleRepository puzzleRepository;
    private final PuzzleCategoryRepository puzzleCategoryRepository;

    @Value("${puzzle.image.url.base}")
    private String imageBaseUrl;

    public PuzzlePictureService(PuzzleRepository puzzleRepository, PuzzleCategoryRepository puzzleCategoryRepository) {
        this.puzzleRepository = puzzleRepository;
        this.puzzleCategoryRepository = puzzleCategoryRepository;
    }

    public List<String> savePictures(List<PictureData> pictureDataList, Long userId, Long familyId) {
        List<String> imageUrls = new ArrayList<>();
        for (PictureData data : pictureDataList) {

            // 1. 이미지 저장
            String uploadDir = "/home/ubuntu/app/images/";
            String fileName = System.currentTimeMillis() + ".png";
            byte[] decodedBytes = Base64.getDecoder().decode(data.getImageBase64());
            try {
                Files.write(Paths.get(uploadDir + fileName), decodedBytes);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패");
            }
            String imageUrl = imageBaseUrl  + fileName;

            // 2. puzzle 테이블 저장
            Puzzle puzzle = new Puzzle();
            puzzle.setImagePath(imageUrl);
            puzzle.setFamiliesId(familyId);
            puzzle.setMessage(data.getComment());
            puzzleRepository.save(puzzle);

            // 3. puzzle_category 연결 (카테고리 값은 이미 DB에 존재한다고 가정)
            PuzzleCategory puzzleCategory = new PuzzleCategory();
            puzzleCategory.setPuzzleId(puzzle.getPuzzleId());
            puzzleCategory.setCategory(data.getCategory());
            puzzleCategoryRepository.save(puzzleCategory);

            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }
}