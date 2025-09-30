package org.dcode.artificialswbackend.puzzle;

import org.dcode.artificialswbackend.puzzle.dto.PictureUploadRequest;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/puzzle/picture")
public class PuzzlePictureController {
    private final PuzzlePictureService puzzlePictureService;

    public PuzzlePictureController(PuzzlePictureService puzzlePictureService) {
        this.puzzlePictureService = puzzlePictureService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPictures(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PictureUploadRequest request) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(JwtUtil.validateAndGetUserId(token));
        Long familyId = JwtUtil.validateAndGetFamilyId(token);
        List<String> imageUrls = puzzlePictureService.savePictures(request.getPictureData(), userId, familyId);
        return ResponseEntity.ok(Map.of("imageUrls", imageUrls));
    }
}

