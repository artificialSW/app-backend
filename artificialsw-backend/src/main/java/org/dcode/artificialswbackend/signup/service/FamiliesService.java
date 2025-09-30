package org.dcode.artificialswbackend.signup.service;

import org.dcode.artificialswbackend.signup.entity.Families;
import org.dcode.artificialswbackend.signup.repository.FamiliesRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class FamiliesService {

    private final FamiliesRepository familiesRepository;
    private final SecureRandom random = new SecureRandom();

    public FamiliesService(FamiliesRepository familiesRepository) {
        this.familiesRepository = familiesRepository;
    }

    public Families createFamily() {
        String code = generateVerificationCode();
        Families family = new Families();
        family.setVerificationCode(code);
        return familiesRepository.save(family);
    }

    private String generateVerificationCode() {
        int code = random.nextInt(1_000_000);
        return String.format("%06d", code);
    }
}
