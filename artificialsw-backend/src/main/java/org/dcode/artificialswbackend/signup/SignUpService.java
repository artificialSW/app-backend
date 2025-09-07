package org.dcode.artificialswbackend.signup;
import org.dcode.artificialswbackend.signup.dto.SignUpRequestDto;
import org.dcode.artificialswbackend.signup.entity.SignUp;
import org.dcode.artificialswbackend.signup.repository.SignUpRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SignUpService {

    private final SignUpRepository signUpRepository;


    public SignUpService(SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
    }

    public void signup(SignUpRequestDto request) {
        SignUp user = new SignUp();
        user.setName(request.getName());
        user.setPhone(request.getPhone()); // 로그인에 사용
        user.setAge(request.getAge());
        if (request.getBirthday() != null) {
            user.setBirthday(LocalDate.parse(request.getBirthday()));
        }
        user.setGender(SignUp.Gender.valueOf(request.getGender()));
        user.setPassword(request.getPassword()); // 평문 주의 실제
        user.setNickname(request.getNickname());
        user.setProfilePhoto(request.getProfilePhoto());
        user.setFamilyType(request.getFamilyType());

        signUpRepository.save(user);
    }
}
