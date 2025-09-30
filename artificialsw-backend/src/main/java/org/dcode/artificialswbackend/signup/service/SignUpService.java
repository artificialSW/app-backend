package org.dcode.artificialswbackend.signup.service;

import org.dcode.artificialswbackend.signup.dto.SignUpRequestDto;
import org.dcode.artificialswbackend.signup.entity.Families;
import org.dcode.artificialswbackend.signup.entity.SignUp;
import org.dcode.artificialswbackend.signup.repository.FamiliesRepository;
import org.dcode.artificialswbackend.signup.repository.SignUpRepository;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SignUpService {
    private final SignUpRepository signUpRepository;
    private final JwtUtil jwtUtil;
    private final FamiliesRepository familiesRepository;

    public SignUpService(SignUpRepository signUpRepository, JwtUtil jwtUtil,  FamiliesRepository familiesRepository) {
        this.signUpRepository = signUpRepository;
        this.jwtUtil = jwtUtil;
        this.familiesRepository = familiesRepository;
    }

    public void signup(SignUpRequestDto request) {
        // 가족 인증번호로 가족 조회
        Families family = familiesRepository.findByVerificationCode(request.getFamilyVerificationCode())
                .orElseThrow(() -> new RuntimeException("Invalid family verification code"));

        SignUp user = new SignUp();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAge(request.getAge());
        if (request.getBirthday() != null) {
            user.setBirthday(LocalDate.parse(request.getBirthday()));
        }
        user.setGender(SignUp.Gender.valueOf(request.getGender()));
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setProfilePhoto(request.getProfilePhoto());
        user.setFamilyType(request.getFamilyType());

        // 가족 ID 설정
        user.setFamilyId(family.getId());

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
