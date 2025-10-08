package org.dcode.artificialswbackend.puzzle;

import jakarta.transaction.Transactional;
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
    @Transactional
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

            // 2. 카테고리 엔티티 조회 (DB에 반드시 존재한다고 가정)
            PuzzleCategory categoryEntity = puzzleCategoryRepository.findByCategory(data.getCategory());
            if (categoryEntity == null) {
                throw new RuntimeException("카테고리가 DB에 존재하지 않습니다: " + data.getCategory());
            }

            // 3. puzzle 테이블 저장, 카테고리 FK로 연결
            Puzzle puzzle = new Puzzle();
            puzzle.setImagePath(imageUrl);
            puzzle.setFamiliesId(familyId);
            puzzle.setMessage(data.getComment());
            puzzle.setCategory(categoryEntity); // 카테고리 엔티티 연결
            puzzleRepository.save(puzzle);

            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }
}