package org.dcode.artificialswbackend.signup.service;

import org.dcode.artificialswbackend.signup.dto.SignUpRequestDto;
import org.dcode.artificialswbackend.signup.entity.Families;
import org.dcode.artificialswbackend.signup.entity.SignUp;
import org.dcode.artificialswbackend.signup.repository.FamiliesRepository;
import org.dcode.artificialswbackend.signup.repository.SignUpRepository;
import org.dcode.artificialswbackend.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class SignUpService {

    private final SignUpRepository signUpRepository;
    private final FamiliesRepository familiesRepository;
    private final JwtUtil jwtUtil;
    private final SecureRandom random = new SecureRandom();

    public SignUpService(SignUpRepository signUpRepository, FamiliesRepository familiesRepository, JwtUtil jwtUtil) {
        this.signUpRepository = signUpRepository;
        this.familiesRepository = familiesRepository;
        this.jwtUtil = jwtUtil;
    }

    public Families signup(SignUpRequestDto request) {
        Families family;

        if (request.getFamilyVerificationCode() == null || request.getFamilyVerificationCode().isEmpty()) {
            // 가족 생성
            family = new Families();
            String newCode = generateVerificationCode();
            family.setVerificationCode(newCode);
            family = familiesRepository.save(family);
        } else {
            // 기존 가족 코드로 조회
            family = familiesRepository.findByVerificationCode(request.getFamilyVerificationCode())
                    .orElseThrow(() -> new RuntimeException("Invalid family verification code"));
        }

        // 회원 생성
        SignUp user = new SignUp();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setAge(request.getAge());
        user.setGender(SignUp.Gender.valueOf(request.getGender()));
        if (request.getBirthday() != null) {
            user.setBirthday(LocalDate.parse(request.getBirthday()));
        }
        user.setFamilyType(request.getFamilyType());
        user.setFamilyId(family.getId());
        user.setProfilePhoto(request.getProfilePhoto());

        signUpRepository.save(user);
        return family;
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
        return jwtUtil.generateToken(user.getId(), user.getFamilyId());
    }

    private String generateVerificationCode() {
        int code = random.nextInt(1_000_000);
        return String.format("%06d", code);
    }
}
