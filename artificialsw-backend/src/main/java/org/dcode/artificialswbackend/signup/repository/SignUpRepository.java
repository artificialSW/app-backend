package org.dcode.artificialswbackend.signup.repository;

import org.dcode.artificialswbackend.signup.entity.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignUpRepository extends JpaRepository<SignUp, Long> {
    Optional<SignUp> findByPhone(String phone);
}
