package org.dcode.artificialswbackend.signup.controller;


import org.dcode.artificialswbackend.signup.dto.LoginRequestDto;
import org.dcode.artificialswbackend.signup.dto.SignUpRequestDto;
import org.dcode.artificialswbackend.signup.service.SignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {

    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("/api/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDto request) {
        signUpService.signup(request);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto request) {
        String result = signUpService.login(request.getId(), request.getPassword());
        return ResponseEntity.ok(result);
    }
}
