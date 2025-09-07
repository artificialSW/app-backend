package org.dcode.artificialswbackend.signup;


import org.dcode.artificialswbackend.signup.dto.LoginRequestDto;
import org.dcode.artificialswbackend.signup.dto.LoginResponseDto;
import org.dcode.artificialswbackend.signup.dto.SignUpRequestDto;
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
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        String token = signUpService.login(request.getId(), request.getPassword());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
