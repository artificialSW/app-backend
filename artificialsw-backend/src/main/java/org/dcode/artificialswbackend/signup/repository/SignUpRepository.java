package org.dcode.artificialswbackend.signup.repository;

import org.dcode.artificialswbackend.signup.entity.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpRepository extends JpaRepository<SignUp, Integer> {
}
