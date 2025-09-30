package org.dcode.artificialswbackend.signup;

import org.dcode.artificialswbackend.signup.dto.SignUpRequestDto;
import org.dcode.artificialswbackend.signup.entity.SignUp;
import org.dcode.artificialswbackend.signup.repository.SignUpRepository;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SignUpService {
    private final SignUpRepository signUpRepository;
    private final JwtUtil jwtUtil;

    public SignUpService(SignUpRepository signUpRepository, JwtUtil jwtUtil) {
        this.signUpRepository = signUpRepository;
        this.jwtUtil = jwtUtil;
    }

    public void signup(SignUpRequestDto request) {
        SignUp user = new SignUp();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAge(request.getAge());
        if (request.getBirthday() != null) {
            user.setBirthday(LocalDate.parse(request.getBirthday()));
        }
        user.setGender(SignUp.Gender.valueOf(request.getGender()));
        user.setPassword(request.getPassword()); // 평문 저장
        user.setNickname(request.getNickname());
        user.setProfilePhoto(request.getProfilePhoto());
        user.setFamilyType(request.getFamilyType());
        signUpRepository.save(user);
    }

    public String login(String phone, String rawPassword) {
        Optional<SignUp> userOpt = signUpRepository.findByPhone(phone);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        SignUp user = userOpt.get();
        if (!rawPassword.equals(user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        // JWT 토큰 발급
        return jwtUtil.generateToken(user.getId(), user.getFamilyId());
    }
}
