package org.dcode.artificialswbackend.puzzle;

import org.dcode.artificialswbackend.puzzle.dto.*;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/puzzle")
public class PuzzlePictureController {
    private final PuzzlePictureService puzzlePictureService;
    private final JwtUtil jwtUtil;

    public PuzzlePictureController(PuzzlePictureService puzzlePictureService,  JwtUtil jwtUtil) {
        this.puzzlePictureService = puzzlePictureService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/picture/upload")
    public ResponseEntity<?> uploadPictures(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PictureUploadRequest request) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        List<String> imageUrls = puzzlePictureService.savePictures(request.getPictureData(), userId, familyId);
        return ResponseEntity.ok(Map.of("imageUrls", imageUrls));
    }

    @PostMapping("/picture/create")
    public ResponseEntity<PuzzleCreateResponse> createPuzzle(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PuzzleCreateRequest request
    ) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.validateAndGetUserId(token);
        Long familyId = jwtUtil.validateAndGetFamilyId(token);

        // 퍼즐 생성 시 userId를 contributors로 전달
        PuzzleCreateResponse response = puzzlePictureService.createPuzzle(request.getSize(), userId,  familyId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{puzzleId}/save-progress")
    public ResponseEntity<?> saveProgress(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("puzzleId") Integer puzzleId,
            @RequestBody SavePuzzleProgressRequest request
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));

        puzzlePictureService.savePuzzleProgress(userId,puzzleId,request);
        return ResponseEntity.ok(Map.of("message", "저장 성공"));
    }


    @GetMapping("/{puzzleId}/progress")
    public ResponseEntity<?> getPuzzleProgress(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer puzzleId) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.validateAndGetUserId(token));

        try {
            PuzzleProgressResponse response = puzzlePictureService.getPuzzleProgress(puzzleId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(423).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{puzzleId}/complete")
    public ResponseEntity<PuzzleCompleteResponse> completePuzzle(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("puzzleId") Integer puzzleId,
            @RequestBody Map<String, Object> body
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        Long solverId = Long.valueOf(String.valueOf(body.get("solverId")));
        int month = Integer.parseInt(String.valueOf(body.get("month")));
        PuzzleCompleteResponse response = puzzlePictureService.completePuzzle(
                puzzleId, solverId, familyId, month
        );
        return ResponseEntity.ok(response);
    }
}

