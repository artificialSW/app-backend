package org.dcode.artificialswbackend.puzzle;

import org.dcode.artificialswbackend.puzzle.dto.PictureUploadRequest;
import org.dcode.artificialswbackend.puzzle.dto.PuzzleCreateRequest;
import org.dcode.artificialswbackend.puzzle.dto.PuzzleCreateResponse;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/puzzle/picture")
public class PuzzlePictureController {
    private final PuzzlePictureService puzzlePictureService;
    private final JwtUtil jwtUtil;

    public PuzzlePictureController(PuzzlePictureService puzzlePictureService,  JwtUtil jwtUtil) {
        this.puzzlePictureService = puzzlePictureService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPictures(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PictureUploadRequest request) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        List<String> imageUrls = puzzlePictureService.savePictures(request.getPictureData(), userId, familyId);
        return ResponseEntity.ok(Map.of("imageUrls", imageUrls));
    }

    @PostMapping("/create")
    public ResponseEntity<PuzzleCreateResponse> createPuzzle(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PuzzleCreateRequest request
    ) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.validateAndGetUserId(token);

        // 퍼즐 생성 시 userId를 contributors로 전달
        PuzzleCreateResponse response = puzzlePictureService.createPuzzle(request.getSize(), userId);

        return ResponseEntity.ok(response);
    }

}

