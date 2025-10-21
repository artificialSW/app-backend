package org.dcode.artificialswbackend.signup.controller;


import org.dcode.artificialswbackend.archive.ArchiveService;
import org.dcode.artificialswbackend.archive.entity.IslandArchives;
import org.dcode.artificialswbackend.archive.repository.IslandArchivesRepository;
import org.dcode.artificialswbackend.signup.dto.LoginRequestDto;
import org.dcode.artificialswbackend.signup.dto.LoginResponseDto;
import org.dcode.artificialswbackend.signup.dto.SignUpRequestDto;
import org.dcode.artificialswbackend.signup.entity.Families;
import org.dcode.artificialswbackend.signup.service.SignUpService;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class SignUpController {

    private final SignUpService signUpService;
    private final JwtUtil jwtUtil;
    private final ArchiveService archiveService;


    public SignUpController(SignUpService signUpService, JwtUtil jwtUtil, ArchiveService archiveService) {
        this.signUpService = signUpService;
        this.jwtUtil = jwtUtil;
        this.archiveService = archiveService;
    }

    @PostMapping("/api/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDto request) {
        Families family = signUpService.signup(request);

        if (request.getFamilyVerificationCode() == null || request.getFamilyVerificationCode().isEmpty()) {
            // 신규 가족 생성 후 회원가입
            return ResponseEntity.ok("회원가입 완료 - 새로운 가족 생성");
        } else {
            // 기존 가족 코드로 회원가입
            return ResponseEntity.ok("회원가입 완료 - 기존 가족 코드로 가입되었습니다.");
        }
    }
    @PostMapping("/api/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        // 1. 로그인 인증 및 JWT 발급
        String jwt = signUpService.login(request.getId(), request.getPassword());
        // 2. JWT에서 familyId 추출
        Long familyId = jwtUtil.validateAndGetFamilyId(jwt);
        // 3. 섬/나무 자동 생성
        archiveService.ensureIslandAndTrees(familyId);
        // 4. 오늘 archiveId 조회
        Long archiveId = archiveService.getTodayArchiveId(familyId);
        // 5. 응답 DTO 반환
        return ResponseEntity.ok(new LoginResponseDto(jwt, archiveId));
    }

}
