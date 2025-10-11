package org.dcode.artificialswbackend.archive;

import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tree")
public class ArchiveController {
    private final ArchiveService archiveService;
    private final JwtUtil jwtUtil;

    public ArchiveController(ArchiveService archiveService, JwtUtil jwtUtil) {
        this.archiveService = archiveService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveCaptureImage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> request
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);

        Long archiveId = Long.valueOf(request.get("archive_id"));
        String imageBase64 = request.get("imageBase64");

        String imageUrl = archiveService.saveCaptureImage(archiveId, familyId, imageBase64);

        return ResponseEntity.ok(Map.of(
                "image_url", imageUrl,
                "message", "저장 완료"
        ));
    }

    @GetMapping("/{archive_id}/scores")
    public ResponseEntity<Map<String, Integer>> getScores(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("archive_id") Long archiveId
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long familyId = jwtUtil.validateAndGetFamilyId(token);
        Map<String, Integer> scores = archiveService.getScores(archiveId, familyId);
        return ResponseEntity.ok(scores);
    }
}