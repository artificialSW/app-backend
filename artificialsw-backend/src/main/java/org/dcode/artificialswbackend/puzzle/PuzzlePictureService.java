package org.dcode.artificialswbackend.puzzle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.dcode.artificialswbackend.puzzle.dto.PictureData;
import org.dcode.artificialswbackend.puzzle.dto.PuzzleCreateResponse;
import org.dcode.artificialswbackend.puzzle.dto.SavePuzzleProgressRequest;
import org.dcode.artificialswbackend.puzzle.entity.Puzzle;
import org.dcode.artificialswbackend.puzzle.entity.PuzzleCategory;
import org.dcode.artificialswbackend.puzzle.repository.PuzzleCategoryRepository;
import org.dcode.artificialswbackend.puzzle.repository.PuzzleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
            puzzle.setFamilies_id(familyId);
            puzzle.setMessage(data.getComment());
            puzzle.setCategory(categoryEntity); // 카테고리 엔티티 연결
            puzzleRepository.save(puzzle);

            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }
    @Transactional
    public PuzzleCreateResponse createPuzzle(int size, String userId) {
        // 1. 랜덤 퍼즐 선택
        Puzzle puzzle = puzzleRepository.findRandomBePuzzleZero();
        if (puzzle == null) throw new RuntimeException("랜덤 퍼즐이 없습니다.");

        // 2. 퍼즐 정보 갱신
        puzzle.setSize(size);
        puzzle.setContributors("[\"" + userId + "\"]");
        puzzle.setBe_puzzle(1); // 반드시 여기서 bePuzzle 값 1로 변경!

        puzzleRepository.save(puzzle);

        return new PuzzleCreateResponse(
                puzzle.getPuzzleId(),
                puzzle.getImagePath(),
                puzzle.getCategory().getCategory(),
                "퍼즐이 생성되었습니다."
        );
    }


    public String saveCaptureImage(String base64Image) {
        String uploadDir = "/home/ubuntu/app/images/capture/";
        String fileName = System.currentTimeMillis() + ".png";

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        try {
            Files.write(Paths.get(uploadDir + fileName), decodedBytes);
        } catch (IOException e) {
            throw new RuntimeException("캡쳐 이미지 저장 실패", e);
        }

        return uploadDir + fileName;
    }

    public Puzzle getPuzzleById(Integer puzzleId) {
        Optional<Puzzle> optionalPuzzle = puzzleRepository.findById(puzzleId);
        if (optionalPuzzle.isEmpty()) {
            throw new RuntimeException("Puzzle not found with id: " + puzzleId);
        }
        return optionalPuzzle.get();
    }

    public void updatePuzzleStatus(Puzzle puzzle, String savedCaptureImagePath, boolean completed, boolean isPlayingPuzzle) {
        puzzle.setCapture_image_path(savedCaptureImagePath);
        puzzle.setCompleted(completed);
        puzzle.setIs_playing_puzzle(isPlayingPuzzle);
    }

    public void updatePuzzlePieces(Puzzle puzzle, Map<String , SavePuzzleProgressRequest.Coordinate> piecesMap) {
        piecesMap.forEach((pieceId, coord) -> {
            puzzle.getPieces().stream()
                    .filter(piece -> piece.getPiece_id().equals(Integer.parseInt(pieceId)))
                    .findFirst()
                    .ifPresent(piece -> {
                        piece.setX(coord.getX());
                        piece.setY(coord.getY());
                    });
        });
    }

    public void updateCompletedPiecesId(Puzzle puzzle, List<Integer> completedPiecesId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(completedPiecesId);
            puzzle.setCompleted_pieces_id(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("completedPiecesId JSON 변환 실패", e);
        }
    }

    // contributor 반영
    public void updateContributor(Puzzle puzzle, Long userId) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 이미 contributors가 있으면 merge, 없으면 새로 추가(최소 한 명 저장)
            String contributorsJson = puzzle.getContributors();
            List<Long> contributorsList;
            if (contributorsJson != null && !contributorsJson.isEmpty()) {
                contributorsList = mapper.readValue(contributorsJson, mapper.getTypeFactory().constructCollectionType(List.class, Long.class));
                if (!contributorsList.contains(userId)) {
                    contributorsList.add(userId);
                }
            } else {
                contributorsList = List.of(userId);
            }
            puzzle.setContributors(mapper.writeValueAsString(contributorsList));
        } catch (Exception e) {
            throw new RuntimeException("Contributors JSON 처리 실패", e);
        }
    }

    @Transactional
    public void savePuzzleProgress(Long userId, SavePuzzleProgressRequest request) {
        Puzzle puzzle = getPuzzleById(request.getPuzzleId());
        String savedCaptureImagePath = saveCaptureImage(request.getCaptureImagePath());
        updatePuzzleStatus(puzzle, savedCaptureImagePath, request.isCompleted(), request.isPlayingPuzzle());
        updatePuzzlePieces(puzzle, request.getPieces());
        updateCompletedPiecesId(puzzle, request.getCompletedPiecesId());
        updateContributor(puzzle, userId); // **기여자 추가**
        puzzleRepository.save(puzzle);
    }

}