package org.dcode.artificialswbackend.mypage;

import org.dcode.artificialswbackend.util.JwtUtil;
import org.dcode.artificialswbackend.mypage.dto.MyPageResponseDto;
import org.dcode.artificialswbackend.mypage.dto.MyPageEditRequestDto;
import org.dcode.artificialswbackend.mypage.dto.MyCommentResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService myPageService;
    private final JwtUtil jwtUtil;

    public MyPageController(MyPageService myPageService, JwtUtil jwtUtil) {
        this.myPageService = myPageService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDto> getMyPage(@RequestHeader("Authorization") String authHeader) {
        try {
            // JWT 토큰에서 Bearer 제거
            String token = authHeader.replace("Bearer ", "");
            
            // 토큰에서 userId 추출
            String userIdStr = jwtUtil.validateAndGetUserId(token);
            Long userId = Long.valueOf(userIdStr);
            
            // 마이페이지 정보 조회
            MyPageResponseDto myPageInfo = myPageService.getMyPageInfo(userId);
            
            return ResponseEntity.ok(myPageInfo);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/mypage/edit")
    public ResponseEntity<String> updateMyPage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MyPageEditRequestDto request) {
        try {
            // JWT 토큰에서 Bearer 제거
            String token = authHeader.replace("Bearer ", "");
            
            // 토큰에서 userId 추출
            String userIdStr = jwtUtil.validateAndGetUserId(token);
            Long userId = Long.parseLong(userIdStr);
            
            // 개인정보 업데이트
            myPageService.updateMyPageInfo(userId, request.getName(), request.getBirth(), request.getFamilyType());
            
            return ResponseEntity.ok("개인정보가 성공적으로 수정되었습니다.");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("개인정보 수정에 실패했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/mypage/comments")
    public ResponseEntity<List<MyCommentResponseDto>> getMyComments(@RequestHeader("Authorization") String authHeader) {
        try {
            // JWT 토큰에서 Bearer 제거
            String token = authHeader.replace("Bearer ", "");
            
            // 토큰에서 userId 추출
            String userIdStr = jwtUtil.validateAndGetUserId(token);
            Long userId = Long.parseLong(userIdStr);
            
            // 내 댓글 조회
            List<MyCommentResponseDto> myComments = myPageService.getMyComments(userId);
            
            return ResponseEntity.ok(myComments);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}