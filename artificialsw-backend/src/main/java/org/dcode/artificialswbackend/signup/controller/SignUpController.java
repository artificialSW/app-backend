package org.dcode.artificialswbackend.signup.controller;


import org.dcode.artificialswbackend.archive.ArchiveService;
import org.dcode.artificialswbackend.signup.dto.LoginRequestDto;
import org.dcode.artificialswbackend.signup.dto.SignUpRequestDto;
import org.dcode.artificialswbackend.signup.service.SignUpService;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        signUpService.signup(request);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto request) {
        // 1. 로그인 인증 및 JWT 발급
        String jwt = signUpService.login(request.getId(), request.getPassword());
        // 2. JWT에서 familyId 추출 (혹은 로그인 서비스에서 반환)
        Long familyId = jwtUtil.validateAndGetFamilyId(jwt);
        // 3. 섬/나무 자동 생성
        archiveService.ensureIslandAndTrees(familyId);
        // 4. JWT 반환
        return ResponseEntity.ok(jwt);
    }
}
