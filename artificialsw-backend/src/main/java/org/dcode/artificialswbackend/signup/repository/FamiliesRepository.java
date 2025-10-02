package org.dcode.artificialswbackend.signup.repository;

import org.dcode.artificialswbackend.signup.entity.Families;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamiliesRepository extends JpaRepository<Families, Long> {
    Optional<Families> findByVerificationCode(String verificationCode);
}