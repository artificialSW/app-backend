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
        Long familyId = jwtUtil.validateAndGetFamilyId(token);

        puzzlePictureService.savePuzzleProgress(userId,puzzleId,familyId,request);
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


    @GetMapping("/in-progress")
    public ResponseEntity<List<PuzzleInProgressResponse>> getInProgressPuzzles(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        List<PuzzleInProgressResponse> responseList = puzzlePictureService.getInProgressPuzzles(familyId);
        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{puzzleId}")
    public ResponseEntity<Map<String, Object>> deletePuzzle(
            @PathVariable("puzzleId") Integer puzzleId) {
        puzzlePictureService.deletePuzzle(puzzleId);
        Map<String, Object> resp = Map.of(
                "puzzleId", puzzleId.toString(),
                "message", "퍼즐이 삭제되었습니다."
        );
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<PuzzleCompletedResponse>> getCompletedPuzzles(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        List<PuzzleCompletedResponse> responseList = puzzlePictureService.getCompletedPuzzles(familyId);
        return ResponseEntity.ok(responseList);
    }


    @PostMapping("/{puzzleId}/retry")
    public ResponseEntity<Map<String, Object>> retryPuzzle(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("puzzleId") Integer puzzleId
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        Map<String, Object> resp = puzzlePictureService.retryPuzzle(puzzleId, familyId);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{puzzleId}/archive")
    public ResponseEntity<Map<String, String>> archivePuzzle(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("puzzleId") Integer puzzleId
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        puzzlePictureService.archivePuzzle(puzzleId, familyId);
        return ResponseEntity.ok(Map.of("message", "퍼즐이 아카이브에 저장되었습니다."));
    }

    @GetMapping("/archive")
    public ResponseEntity<List<PuzzleArchiveResponse>> getArchivedPuzzles(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        List<PuzzleArchiveResponse> responses = puzzlePictureService.getArchivedPuzzles(familyId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{puzzleArchiveId}/archive")
    public ResponseEntity<Map<String, Object>> deleteArchivedPuzzle(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("puzzleArchiveId") Long puzzleArchiveId
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        puzzlePictureService.deleteArchivedPuzzle(familyId, puzzleArchiveId);
        return ResponseEntity.ok(Map.of(
                "puzzle_archive_id", puzzleArchiveId.toString(),
                "message", "퍼즐이 아카이브에서 삭제되었습니다."
        ));
    }
}

